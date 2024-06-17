/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.processors;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zalo.profile.thrift.TValue;
import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.wrapper.profile.ProfileModelV2;
import com.vng.zing.zgroupmedia.thrift.EMediaType;
import com.vng.zing.zgroupmedia.thrift.TListMediaResult;
import com.vng.zing.zgroupmedia.thrift.TMedia;
import com.vng.zing.zgroupmedia.thrift.TOperation;
import com.vng.zing.zgroupmedia.thrift.TOperationResult;
import com.vng.zing.zgroupmedia.wrapper.ZGroupMediaMWClient;
import com.vng.zing.zgroupmedialib.common.constant.CommonConstant;
import com.vng.zing.zgroupmedialib.processor.filterversion.FilterVersionUtils;
import com.vng.zing.zgroupmedialib.processor.group.GroupUtils;
import com.vng.zing.zgroupmedialib.processor.notification.interactivemsginfo.InteractiveMsgInfo;
import com.vng.zing.zgroupmedialib.processor.notification.zinstantmsg.ZSendMsgInstProcessor;
import static com.vng.zing.zgroupmediamemoryworker.model.GroupMediaMemoryModel.memoryClient;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.sharememory.ShareMemory;
import com.vng.zing.zprawapimw.thrift.TSendMessageResult;
import com.vng.zing.zprojectlib.common.utils.ListUtils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

/**
 *
 * @author sangvv2
 */
public class ShareMemoryProcessor {

	private static Class thisClass = ShareMemoryProcessor.class;
	private static final Logger _Logger = ZLogger.getLogger(ShareMemoryProcessor.class);

	private static final List<Integer> LIST_GROUP_TEST;

	static {
		int[] whiteListGroups = ZConfig.Instance.getIntArray("Config.white_list_group", new int[0]);
		LIST_GROUP_TEST = new ArrayList<>();
		for (int group : whiteListGroups) {
			LIST_GROUP_TEST.add(group);
		}
	}

	int userId;
	int groupId;
	List<Long> fileIds;
	String caption;
	byte language;
	int operationId;
	JSONObject logParam;

	ApiMessage res;
	StatusCode statusCode;

	ThreadProfiler profiler;

	public ShareMemoryProcessor(int userId, int groupId, List<Long> fileIds, String caption, byte language,
		int operationId, JSONObject logParam) {
		this.userId = userId;
		this.groupId = groupId;
		this.fileIds = fileIds;
		this.caption = caption;
		this.language = language;
		this.operationId = operationId;
		this.logParam = logParam;

		res = ApiMessage.getSuccessMsg();
		profiler = Profiler.getThreadProfiler();
	}

	private enum StatusCode {
		UNKNOWN_EXCEPTION(-1),
		SUCCESS(0),
		NOT_PERMIT(1);

		public int value;

		private StatusCode(int value) {
			this.value = value;
		}
	}

	public ApiMessage process() {
		String thisFunction = "process";
		profiler.push(thisClass, thisFunction);
		try {
			statusCode = shareMemoryExecute();
			switch (statusCode) {
				case NOT_PERMIT:
					res = ApiMessage.PERMISSION_EXCEPTION;
					break;
				case UNKNOWN_EXCEPTION:
					res = ApiMessage.UNKNOWN_EXCEPTION;
					break;
				case SUCCESS:
					ShareMemory data = buildJson();
					res.setData(data);
					break;
			}
		} catch (Exception ex) {
			res = ApiMessage.UNKNOWN_EXCEPTION;
			_Logger.error("Share memory failed! userId: " + userId
				+ ", groupId: " + groupId
				+ ", fileIds: " + ListUtils.toString(fileIds)
				+ ", caption: " + caption
				+ ", language: " + language
				+ ". Ex: " + ex, ex);
		} finally {
			profiler.pop(thisClass, thisFunction);
		}
		return res;
	}

	private StatusCode shareMemoryExecute() {
//		if (!LIST_GROUP_TEST.contains(groupId)) {
//			return StatusCode.UNKNOWN_EXCEPTION;
//		}

		if (!GroupUtils.checkUserInGroup(userId, groupId)) {
			return StatusCode.NOT_PERMIT;
		}

		TListMediaResult listMediaRes = ZGroupMediaMWClient.MEDIA_INSTANCE
			.multiGetMedia(userId, groupId, EMediaType.MEDIA, fileIds);
		if (listMediaRes.error < 0 || listMediaRes.value == null) {
			_Logger.error("Get list media failed! userId: " + userId
				+ ", groupId: " + groupId
				+ ", mediaIds: " + ListUtils.toString(fileIds)
				+ ". Error: " + listMediaRes.error);
			return StatusCode.UNKNOWN_EXCEPTION;
		}

		// check media permission
		List<TMedia> medias = filterListMedia(listMediaRes.value);
		if (medias == null || medias.isEmpty()) {
			_Logger.error("medias is empty!");
			return StatusCode.UNKNOWN_EXCEPTION;
		}

		// send zinstant msg
		List<Integer> userZMsgInst = new ArrayList();
		List<Integer> userMsgInfo = new ArrayList();
		FilterVersionUtils.filterEnableUserShareMemory(groupId, userZMsgInst, userMsgInfo);

		Set<Integer> property = new HashSet<>();
		property.add(CommonConstant.MULTI_DARK);
		long zInstantMsgId = sendZMsgInst(userZMsgInst, medias, property);
		JSONObject msg = new JSONObject();
		msg.put("zInstantMsgId", zInstantMsgId);

		// send msg info for user not compatibility with zinstant
		long infoMsgId = sendMsgInfo(userMsgInfo);
		msg.put("infoMsgId", infoMsgId);

		logParam.put("msg", msg);
		if (zInstantMsgId > 0 || infoMsgId > 0) {
			return StatusCode.SUCCESS;
		}
		return StatusCode.UNKNOWN_EXCEPTION;
	}

	private long sendZMsgInst(List<Integer> userZMsgInstant, List<TMedia> medias, Set<Integer> property) {
		if (userZMsgInstant == null || userZMsgInstant.isEmpty()) {
			return -1;
		}

		TOperation operation = null;
		if (operationId > 0) {
			TOperationResult operationRet = memoryClient.getOperation(operationId);
			if (operationRet.error < 0 || operationRet.operation == null) {
				_Logger.error(String.format("Get operation failed | ret %s operationId %s",
					operationRet.error, operationId));
			}
			operation = operationRet.operation;
		}

		TSendMessageResult sendMsgRes = ZSendMsgInstProcessor.INSTANCE
			.sendShareMemoryMsg(userId, groupId, medias, userZMsgInstant, caption, language, operation,
				property);

		if (sendMsgRes.error < 0) {
			_Logger.error("Send zinstant share memory failed! userId: " + userId
				+ ", groupId: " + groupId
				+ ", fileIds: " + ListUtils.toString(fileIds)
				+ ", userIds: " + ListUtils.toString(userZMsgInstant)
				+ ", caption: " + caption
				+ ", language: " + language
				+ ". Error: " + sendMsgRes.error);
		}
		return sendMsgRes.msgId;
	}

	private long sendMsgInfo(List<Integer> userInteractiveMsgInfo) {
		if (userInteractiveMsgInfo == null || userInteractiveMsgInfo.isEmpty()) {
			return -1;
		}

		TValue profile = ProfileModelV2.INSTANCE.getProfile(userId);
		if (profile == null) {
			return -1;
		}

		long interactiveMsgId = InteractiveMsgInfo.shareMemory(profile, groupId, userInteractiveMsgInfo);
		if (interactiveMsgId < 0) {
			_Logger.error("Send msg info failed! userId: " + userId
				+ ", groupId: " + groupId
				+ ", userIds: " + ListUtils.toString(userInteractiveMsgInfo)
				+ ". Error: " + interactiveMsgId);
		}
		return interactiveMsgId;
	}

	private List<TMedia> filterListMedia(List<TMedia> medias) {
		List<TMedia> filterRes = new ArrayList<>();
		for (TMedia media : medias) {
			if (media.groupId == groupId) {
				filterRes.add(media);
			}
		}
		return filterRes;
	}

	private ShareMemory buildJson() {
		ShareMemory shareMemory = new ShareMemory();
		shareMemory.setStatusCode(statusCode.value);
		return shareMemory;
	}
}
