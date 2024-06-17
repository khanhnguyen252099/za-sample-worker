/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.processors;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zalo.profile.thrift.TValue;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.wrapper.profile.ProfileModelV2;
import com.vng.zing.zcommon.thrift.ECode;
import com.vng.zing.zgroupmedia.thrift.EMemoriesStatus;
import com.vng.zing.zgroupmedia.thrift.TExtDetail;
import com.vng.zing.zgroupmedia.thrift.TMedia;
import com.vng.zing.zgroupmedia.thrift.TPhoto;
import com.vng.zing.zgroupmedia.thrift.TMemoriesResult;
import com.vng.zing.zgroupmedia.thrift.TOperation;
import com.vng.zing.zgroupmedialib.common.utils.MediaUtils;
import com.vng.zing.zgroupmedialib.processor.notification.zinstantmsg.sharememory.ZInstantShareMemory;
import static com.vng.zing.zgroupmediamemoryworker.model.GroupMediaMemoryModel.memoryClient;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getrandommemories.ContentItem;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getrandommemories.CustomTitle;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getrandommemories.GetRandomMemories;
import com.vng.zing.zprojectlib.common.constant.LanguageConstant;
import static com.vng.zing.zprojectlib.common.constant.LanguageConstant.languageByteToStringHashMap;
import com.vng.zing.zprojectlib.common.log.Log4j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.log4j.Logger;

/**
 *
 * @author sangvv2
 */
public class RandomMemoriesProcessor {

	private static final Class thisClass = RandomMemoriesProcessor.class;
	private static final Logger _Logger = ZLogger.getLogger(RandomMemoriesProcessor.class);

	int userId;
	int groupId;
	int subCMD;
	int operationId;
	String language;
	ApiMessage res;

	ThreadProfiler profiler;
	TMemoriesResult memoriesResult;

	public RandomMemoriesProcessor(int subCMD, int userId, int groupId, byte bLanguage, int operationId) {
		this.userId = userId;
		this.groupId = groupId;
		this.subCMD = subCMD;
		this.operationId = operationId;
		this.language = languageByteToStringHashMap.get(bLanguage);
		res = ApiMessage.getSuccessMsg();
		profiler = Profiler.getThreadProfiler();
	}

	public ApiMessage process() {
		String thisFunction = "process";
		profiler.push(thisClass, thisFunction);
		try {
			memoriesResult = getMemory();
			if (memoriesResult.error < 0) {
				if (memoriesResult.error == -ECode.NOT_PERMIT.getValue()) {
					res = ApiMessage.PERMISSION_EXCEPTION;
				} else {
					res = ApiMessage.UNKNOWN_EXCEPTION;
				}
			} else {
				GetRandomMemories data = buildJson();
				res.setData(data);
			}
		} catch (Exception ex) {
			res = ApiMessage.UNKNOWN_EXCEPTION;
			_Logger.error("Get groups follow memory failed! userId: " + userId
				+ ", groupId: " + groupId
				+ ". Ex: " + ex, ex);
		} finally {
			profiler.pop(thisClass, thisFunction);
			Log4j.log("random memories result | userId %s groupId %s ret %s", userId, groupId, ApiMessage.toJsonString(res));
		}
		return res;
	}

	private GetRandomMemories buildJson() {
		GetRandomMemories data = new GetRandomMemories();
		data.setStatusCode(memoriesResult.statusCode.getValue());

		if (memoriesResult.statusCode == EMemoriesStatus.OK) {
			TOperation operation = memoriesResult.operation;
			if (operation != null) {
				data.setExpireTime(operation.endTime);
				data.setOperationId(operation.operationId);
			}

			List<ContentItem> content = buildContent();
			data.setContent(content);
		}

		return data;
	}

	private List<ContentItem> buildContent() {
		List<ContentItem> content = new ArrayList<>();
		Map<Integer, TValue> mapProfile = getMapProfile();
		for (TMedia media : memoriesResult.memories) {
			ContentItem contentItem = buildContentItem(media, mapProfile);
			content.add(contentItem);
		}
		return content;
	}

	private ContentItem buildContentItem(TMedia media, Map<Integer, TValue> mapProfile) {
		ContentItem contentItem = new ContentItem();

		TOperation operation = memoriesResult.operation;
		if (operation != null && operation.title != null
			&& !operation.title.get(LanguageConstant.VIETNAMESE).equals("")) {
			CustomTitle customTitle = new CustomTitle();
			customTitle.setEn(operation.title.get(LanguageConstant.ENGLISH));
			customTitle.setVi(operation.title.get(LanguageConstant.VIETNAMESE));
			contentItem.setCustomTitle(customTitle);
		} else {
			List<TMedia> mediaList = Arrays.asList(media);
			Map<String, String> titleMap = ZInstantShareMemory.INSTANCE.getMapTitleByCreatedTimeV2(mediaList);
			CustomTitle customTitle = new CustomTitle(titleMap.getOrDefault(LanguageConstant.ENGLISH, ""),
				titleMap.getOrDefault(LanguageConstant.VIETNAMESE, ""));
			contentItem.setCustomTitle(customTitle);
		}

		TValue profile = mapProfile.get(media.ownerId);
		if (profile != null) {
			contentItem.setDisplayName(profile.displayName);
		}

		contentItem.setGlobalMsgId(media.globalMsgId);
		contentItem.setOwnerId(media.ownerId);
		contentItem.setClientMsgId(media.clientMsgId);
		contentItem.setCreatedTime(media.createdTime);
		contentItem.setSrcUrl(media.srcUrl);
		contentItem.setThumbUrl(media.thumbUrl);
		contentItem.setFileId(media.globalId);

		TExtDetail ext = media.getExt();
		if (ext == null) {
			return contentItem;
		}

		TPhoto photo = ext.getPhoto();
		if (photo != null) {
			contentItem.setSubType(photo.subType.toString().toLowerCase());
			contentItem.setHdUrl(photo.hdUrl);
			contentItem.setWidth(photo.width);
			contentItem.setHeight(photo.height);
			contentItem.setDesc(photo.title);
			String location = MediaUtils.getLocationOfPhoto(photo, language);
			if (location != null) {
				contentItem.setLocation(location);
			}
		}

		return contentItem;
	}

	private Map<Integer, TValue> getMapProfile() {
		Set<Integer> ownerIds = new HashSet<>();
		for (TMedia media : memoriesResult.memories) {
			ownerIds.add(media.ownerId);
		}

		profiler.push(thisClass, "getMultiProfile");
		try {
			return ProfileModelV2.INSTANCE.multiGet(new ArrayList<>(ownerIds));
		} finally {
			profiler.pop(thisClass, "getMultiProfile");
		}
	}

	private TMemoriesResult getMemory() {
		if (operationId > 0) {
			return memoryClient.getMemories(userId, groupId, 1, operationId);
		} else {
			return memoryClient.getMemories(userId, groupId, 0, -1);
		}
	}
}
