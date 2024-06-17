/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.functions;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.groupmw.thrift.TMiniGroupInfo;
import com.vng.zing.zgroupmedialib.common.abstractclass.AbstractGroupMediaFunc;
import com.vng.zing.zgroupmedialib.common.constant.CommonConstant;
import static com.vng.zing.zgroupmedialib.common.constant.CommonConstant.DIRTY_GROUP;
import com.vng.zing.zgroupmedialib.common.testcase.TestConst;
import com.vng.zing.zgroupmedialib.common.utils.HistoryFilterUtils;
import com.vng.zing.zgroupmedialib.common.utils.ZBufferWrapper;
import com.vng.zing.zgroupmedialib.processor.group.GroupUtils;
import com.vng.zing.zgroupmediamemoryworker.model.GroupMediaDB;
import com.vng.zing.zgroupmediamemoryworker.processors.RandomMemoriesProcessor;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author sangvv2
 */
public class RandomMemoriesFunc extends AbstractGroupMediaFunc {

	private static final Class ThisClass = RandomMemoriesFunc.class;

	private static final int MIN_NUM_BYTE_OF_PARAM = 15;

	public static final Set<Integer> property = new HashSet<>();

	static {
		property.add(CommonConstant.FILTER_HISTORY);
	}

	public RandomMemoriesFunc() {
		super(ThisClass, MIN_NUM_BYTE_OF_PARAM);
	}

	@Override
	protected ApiMessage processNormalCase() {
		apiMessage = processInput();
		prepareLogEntry();
		return apiMessage;

	}

	private ApiMessage processInput() {
		ZBufferWrapper zbuffer = new ZBufferWrapper(params);
		int userId = srcHdr.getSourceId();

		byte clientType = zbuffer.readI8();
		_logParams.put("clientType", clientType);
		logEntry.setClientType(clientType);

		int clientVersion = zbuffer.readI32();
		_logParams.put("clientVersion", clientVersion);
		logEntry.setClientVersion(clientVersion);

		byte[] trackingData = zbuffer.readBytesS4();
		_logParams.put("trackingData", trackingData);

		byte language = zbuffer.readI8();
		_logParams.put("language", language);

		byte source = zbuffer.readI8();
		_logParams.put("source", source);

		int groupId = zbuffer.readI32();
		_logParams.put("groupId", groupId);

//		Map<Integer, TMiniGroupInfo> ret = GroupUtils.multiGetMiniGroupInfo(
//				Arrays.asList(groupId));
//
//		boolean checkBlockMedia = GroupUtils.checkBlockMedia(groupId, ret);
//
//		boolean enableHistoryMsg = true;
//		boolean passByEnableHistoryMsg = HistoryFilterUtils.passByEnableHistoryMsg(userId,
//				groupId, false, property);
//		if (!passByEnableHistoryMsg) {
//			if (ret != null) {
//				enableHistoryMsg = GroupUtils.checkEnableHistoryMsg(groupId, ret);
//			}
//		}
//
//		boolean dirtyGroup = GroupUtils.isDirtyGroup(groupId, ret);
//
//		if (dirtyGroup || !enableHistoryMsg || checkBlockMedia) {
//			return ApiMessage.getSuccessMsg();
//		}
//
//		int operationId = -1;
//		if (srcHdr.getSubCommand() == (byte) 1) {
//			operationId = zbuffer.readI32();
//			_logParams.put("operationId", operationId);
//		}
//		return new RandomMemoriesProcessor(srcHdr.getSubCommand(), userId, groupId, language, operationId).
//				process();
		return new ApiMessage(-69, "Failed");
	}
}
