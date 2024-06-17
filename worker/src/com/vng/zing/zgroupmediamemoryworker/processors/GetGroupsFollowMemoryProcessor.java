/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.processors;

import com.google.gson.Gson;
import com.vng.wmb.server.api.ApiMessage;
import com.vng.zing.configer.ZConfig;
import com.vng.zing.logger.ZLogger;
import com.vng.zing.stats.Profiler;
import com.vng.zing.stats.ThreadProfiler;
import com.vng.zing.zcommon.thrift.ECode;
import com.vng.zing.zgroupmedia.thrift.TGroupsFollowMemoryResult;
import static com.vng.zing.zgroupmediamemoryworker.model.GroupMediaMemoryModel.memoryClient;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getgroupsfollowmemory.Content;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getgroupsfollowmemory.GetGroupsFollowMemory;
import com.vng.zing.zgroupmediamemoryworker.ouputjson.getgroupsfollowmemory.GroupItem;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

/**
 *
 * @author sangvv2
 */
public class GetGroupsFollowMemoryProcessor {

	private static Class thisClass = GetGroupsFollowMemoryProcessor.class;
	private static final Logger _Logger = ZLogger.getLogger(GetGroupsFollowMemoryProcessor.class);

	int userId;
	int offset;
	int size;
	int lastGroupId;
	ApiMessage res;

	ThreadProfiler profiler;
	TGroupsFollowMemoryResult followRet;

	private static final int GROUP_ID_DUMMY = 1;
	private static final GetGroupsFollowMemory EMPTY_DATA;

	static {
		EMPTY_DATA = new GetGroupsFollowMemory();
		Content content = new Content();
		content.setGroups(new ArrayList<>());
		content.setIsLoadMore(false);
		content.setTotal(0);
		content.setLastId(0);
		EMPTY_DATA.setContent(content);
	}

	public GetGroupsFollowMemoryProcessor(int userId, int offset, int size, int lastGroupId) {
		this.userId = userId;
		this.offset = offset;
		this.size = size;
		this.lastGroupId = lastGroupId;
		res = ApiMessage.getSuccessMsg();
		profiler = Profiler.getThreadProfiler();
	}

	public ApiMessage process() {
		String thisFunction = "process";
		profiler.push(thisClass, thisFunction);
		try {
			res.setData(EMPTY_DATA);
//			else{
//				followRet = memoryClient.getGroupsFollowMemory(userId, offset, size, lastGroupId);
//				if (followRet.error < 0) {
//					if (followRet.error == -ECode.NOT_PERMIT.getValue()) {
//						res = ApiMessage.PERMISSION_EXCEPTION;
//					}
//					else {
//						res = ApiMessage.UNKNOWN_EXCEPTION;
//					}
//				}
//				else {
//					GetGroupsFollowMemory data = buildJson();
//					res.setData(data);
//				}
//			}
		}
		catch (Exception ex) {
			res = ApiMessage.UNKNOWN_EXCEPTION;
			_Logger.error("Get groups follow memory failed! userId: " + userId
					+ ", offset: " + offset
					+ ", size: " + size
					+ ", lastGroupId: " + lastGroupId
					+ ". Ex: " + ex, ex);
		}
		finally {
			profiler.pop(thisClass, thisFunction);
		}
		return res;
	}

	private GetGroupsFollowMemory buildJson() {
		GetGroupsFollowMemory data = new GetGroupsFollowMemory();
		Content content = new Content();

		boolean isLoadMore = true;
		if ((offset + size) >= followRet.total) {
			isLoadMore = false;
		}

		List<GroupItem> groups = new ArrayList<>();
		if (followRet.satisfyGroupIds != null) {
			for (int groupId : followRet.satisfyGroupIds) {
				groups.add(new GroupItem(groupId, true));
			}
		}

		if (followRet.notSatisfyGroupIds != null) {
			for (int groupId : followRet.notSatisfyGroupIds) {
				groups.add(new GroupItem(groupId, false));
			}
		}

		// hot fix for android
		if ((followRet.satisfyGroupIds == null || followRet.satisfyGroupIds.isEmpty())
				&& (followRet.notSatisfyGroupIds == null || followRet.notSatisfyGroupIds.isEmpty())) {
			groups.add(new GroupItem(GROUP_ID_DUMMY, false));
		}

		content.setTotal(followRet.total);
		content.setIsLoadMore(isLoadMore);
		content.setLastId(followRet.lastGroupId);
		content.setGroups(groups);

		data.setContent(content);
		return data;
	}
}
