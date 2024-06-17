/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.model;

import com.vng.zing.configer.ZConfig;
import com.vng.zing.stats.Profiler;
import com.vng.zing.wrapper.zgroupmediamemorymw.ZGroupMediaMemoryMWClient;

/**
 *
 * @author sangvv2
 */
public class GroupMediaMemoryModel {

	private static Class thisClass = GroupMediaMemoryModel.class;
	public static final GroupMediaMemoryModel Instance = new GroupMediaMemoryModel();

	public static final ZGroupMediaMemoryMWClient memoryClient = new ZGroupMediaMemoryMWClient("group_media_memory");
}
