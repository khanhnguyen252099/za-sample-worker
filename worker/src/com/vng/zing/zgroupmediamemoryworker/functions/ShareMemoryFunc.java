/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.functions;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.stats.Profiler;
import com.vng.zing.zgroupmedialib.common.abstractclass.AbstractGroupMediaFunc;
import static com.vng.zing.zgroupmedialib.common.constant.CommonConstant.IOS;
import com.vng.zing.zgroupmedialib.common.utils.ZBufferWrapper;
import com.vng.zing.zgroupmediamemoryworker.processors.RandomMemoriesProcessor;
import com.vng.zing.zgroupmediamemoryworker.processors.ShareMemoryProcessor;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 *
 * @author sangvv2
 */
public class ShareMemoryFunc extends AbstractGroupMediaFunc {

	private static final Class ThisClass = ShareMemoryFunc.class;

	private static final int MIN_NUM_BYTE_OF_PARAM = 24;
	private static final ApiMessage HOT_FIX;

	static {
		JSONObject json = new JSONObject();
		json.put("statusCode", -1);
		HOT_FIX = new ApiMessage(json);
	}

	public ShareMemoryFunc() {
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

		byte shareType = zbuffer.readI8();
		_logParams.put("shareType", shareType);

		ArrayList<Long> fileIds = new ArrayList<>();
		zbuffer.readListI64S4(fileIds);
		_logParams.put("fileIds", fileIds);

		String caption = zbuffer.readStringS4();
		_logParams.put("caption", caption);

//		int subCmd = srcHdr.getSubCommand();
//		int operationId = 0;
//		if (subCmd == 1) {
//			operationId = zbuffer.readI32();
//			_logParams.put("operationId", operationId);
//		}
//
//		if (clientType == IOS && clientVersion < 338 && caption != null && !caption.isEmpty()
//				&& operationId != 0) {
//			Profiler.getThreadProfiler().push(ThisClass, "hotFix");
//			Profiler.getThreadProfiler().pop(ThisClass, "hotFix");
//			return HOT_FIX;
//		}
//		return new ShareMemoryProcessor(userId, groupId, fileIds, caption, language, operationId, _logParams).process();
		return new ApiMessage(-69,"Failed");
	}
}
