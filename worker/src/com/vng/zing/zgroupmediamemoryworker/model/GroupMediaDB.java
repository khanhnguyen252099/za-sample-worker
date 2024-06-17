/*
 *  Copyright (c) 2012-2018 by Zalo Group.
 *  All Rights Reserved.
 */
package com.vng.zing.zgroupmediamemoryworker.model;

import com.vng.zing.list64.thrift.wrapper.List64Client;
import com.vng.zing.zprojectlib.wrapper.db.StrList32BitMap;

/**
 *
 * @author hieund3
 */
public class GroupMediaDB {

	public static final List64Client cliPhotoIdList = new List64Client("photoId_globalId");
	public static final StrList32BitMap dirtyGroupIdSet = new StrList32BitMap("dirty_groupId");

	public static void init() {

	}
}
