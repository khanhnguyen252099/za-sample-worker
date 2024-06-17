/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.functions;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.zgroupmedialib.common.abstractclass.AbstractGroupMediaFunc;
import com.vng.zing.zgroupmedialib.common.utils.ZBufferWrapper;
import com.vng.zing.zgroupmediamemoryworker.processors.GetGroupsFollowMemoryProcessor;

/**
 *
 * @author sangvv2
 */
public class GetGroupsFollowMemoryFunc extends AbstractGroupMediaFunc {

	private static final Class ThisClass = GetGroupsFollowMemoryFunc.class;

	private static final int MIN_NUM_BYTE_OF_PARAM = 23;

	public GetGroupsFollowMemoryFunc() {
		super(ThisClass, MIN_NUM_BYTE_OF_PARAM);
	}

	@Override
	protected ApiMessage processNormalCase() {
		apiMessage = processInput();
//		prepareLogEntry();
		logEntry.setParam(_logParams.toJSONString());
		logEntry.setResult(0);
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

		int offset = zbuffer.readI32();
		_logParams.put("offset", offset);

		int size = zbuffer.readI32();
		_logParams.put("size", size);

		int lastGroupId = zbuffer.readI32();
		_logParams.put("lastGroupId", lastGroupId);
		if (clientType == (byte) 1) {
			return ApiMessage.METHOD_NOT_FOUND;
		}
		else {
			return new GetGroupsFollowMemoryProcessor(userId, offset, size, lastGroupId).process();
		}

	}
}
