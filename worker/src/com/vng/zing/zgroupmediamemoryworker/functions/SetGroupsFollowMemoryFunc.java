/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.functions;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.zgroupmedialib.common.abstractclass.AbstractGroupMediaFunc;
import com.vng.zing.zgroupmedialib.common.utils.ZBufferWrapper;
import com.vng.zing.zgroupmediamemoryworker.processors.SetGroupsFollowMemoryProcessor;
import java.util.ArrayList;

/**
 *
 * @author sangvv2
 */
public class SetGroupsFollowMemoryFunc extends AbstractGroupMediaFunc {

	private static final Class ThisClass = SetGroupsFollowMemoryFunc.class;

	private static final int MIN_NUM_BYTE_OF_PARAM = 13;

	public SetGroupsFollowMemoryFunc() {
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

		byte bAction = zbuffer.readI8();
		_logParams.put("action", bAction);

		ArrayList<Integer> groupIds = new ArrayList<>();
		zbuffer.readListI32S1(groupIds);
		_logParams.put("groupIds", groupIds);

//		return new SetGroupsFollowMemoryProcessor(userId, bAction, groupIds).process();
		return new ApiMessage(-69,"Failed");

	}
}
