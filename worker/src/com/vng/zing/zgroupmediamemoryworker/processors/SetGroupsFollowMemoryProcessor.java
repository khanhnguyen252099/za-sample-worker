/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.processors;

import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.zcommon.thrift.ECode;
import com.vng.zing.zgroupmedia.thrift.EActionFollowMemory;
import com.vng.zing.zgroupmedia.thrift.TSetGroupsFollowMemoryResult;
import static com.vng.zing.zgroupmediamemoryworker.model.GroupMediaMemoryModel.memoryClient;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.setgroupsfollowmemory.Content;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.setgroupsfollowmemory.SetGroupsFollowMemory;
import com.vng.zing.zprojectlib.common.utils.ListUtils;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sangvv2
 */
public class SetGroupsFollowMemoryProcessor {

	private static Class thisClass = SetGroupsFollowMemoryProcessor.class;
	private static final Logger _Logger = ZLogger.getLogger(SetGroupsFollowMemoryProcessor.class);

	int userId;
	byte bAction;
	EActionFollowMemory action;
	List<Integer> groupIds;
	ApiMessage res;

	ThreadProfiler profiler;
	TSetGroupsFollowMemoryResult setGroupsFollowMemoryResult;

	public SetGroupsFollowMemoryProcessor(int userId, byte bAction, List<Integer> groupIds) {
		this.userId = userId;
		this.bAction = bAction;
		this.action = mapAction(bAction);
		this.groupIds = groupIds;
		res = ApiMessage.getSuccessMsg();
		profiler = Profiler.getThreadProfiler();
	}

	public ApiMessage process() {
		String thisFunction = "process";
		profiler.push(thisClass, thisFunction);
		try {
			setGroupsFollowMemoryResult = memoryClient.setGroupsFollowMemory(userId, action, groupIds);
			if (setGroupsFollowMemoryResult.error < 0) {
				if (setGroupsFollowMemoryResult.error == -ECode.NOT_PERMIT.getValue()) {
					res = ApiMessage.PERMISSION_EXCEPTION;
				} else {
					res = ApiMessage.UNKNOWN_EXCEPTION;
				}
			} else {
				SetGroupsFollowMemory data = buildJson();
				res.setData(data);
			}
		} catch (Exception ex) {
			res = ApiMessage.UNKNOWN_EXCEPTION;
			_Logger.error("Get groups follow memory failed! userId: " + userId
				+ ", action: " + action
				+ ", groupIds: " + ListUtils.toString(groupIds)
				+ ". Ex: " + ex, ex);
		} finally {
			profiler.pop(thisClass, thisFunction);
		}
		return res;
	}

	private SetGroupsFollowMemory buildJson() {
		SetGroupsFollowMemory data = new SetGroupsFollowMemory();
		Content content = new Content();

		List<Integer> failed = setGroupsFollowMemoryResult.failed == null
			? new ArrayList<>()
			: setGroupsFollowMemoryResult.failed;

		List<Integer> groupHasMemory = setGroupsFollowMemoryResult.groupHasMemory == null
			? new ArrayList<>()
			: setGroupsFollowMemoryResult.groupHasMemory;

		content.setFailed(failed);
		content.setStatusCode(mapStatusCode(setGroupsFollowMemoryResult.failed));
		content.setGroupHasMemory(groupHasMemory);
		data.setContent(content);
		return data;
	}

	// 0: least 1 group setting successful
	// 1: there is no groups successful
	private int mapStatusCode(List<Integer> failed) {
		if (failed == null || failed.isEmpty()) {
			if (groupIds == null || groupIds.isEmpty()) {
				return 1;
			}
			return 0;
		}
		if (failed.size() == groupIds.size()) {
			return 1;
		}
		return 0;
	}

	private EActionFollowMemory mapAction(byte bAction) {
		switch (bAction) {
			case -1:
				return EActionFollowMemory.UNFOLLOW_ALL;

			case 0:
				return EActionFollowMemory.UNFOLLOW;

			case 1:
				return EActionFollowMemory.FOLLOW;
		}
		return null;
	}
}
