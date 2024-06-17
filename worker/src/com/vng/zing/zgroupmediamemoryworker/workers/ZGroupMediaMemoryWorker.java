/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.zgroupmediamemoryworker.workers;

import com.vng.wmb.worker.IZaloFunction;
import com.vng.wmb.worker.client.ZaloWorkerConfig;
import com.vng.wmb.worker.client.ZaloWorkerController;
import com.vng.zing.stats.CustomStats;
import com.vng.zing.stats.Profiler;
import com.vng.zing.zgroupmediamemoryworker.common.constant.CMDs;
import com.vng.zing.zgroupmediamemoryworker.functions.GetGroupsFollowMemoryFunc;
import com.vng.zing.zgroupmediamemoryworker.functions.RandomMemoriesFunc;
import com.vng.zing.zgroupmediamemoryworker.functions.SetGroupsFollowMemoryFunc;
import com.vng.zing.zgroupmediamemoryworker.functions.ShareMemoryFunc;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

/**
 *
 * @author sangvv2
 */
public class ZGroupMediaMemoryWorker extends Thread {

	private static final Class ThisClass = ZGroupMediaMemoryWorker.class;
	private static final Logger _Logger = Logger.getLogger(ThisClass);

	///
	private static final Controller workerController = new Controller();
	private final long sleepTime = 10000; // Check connection alive every 30 secs
	private final String startingMsg = "Group-worker service started";
	private boolean started = false;

	///
	public static final ZGroupMediaMemoryWorker Instance = new ZGroupMediaMemoryWorker();

	public static class Controller extends ZaloWorkerController {

		public Controller() {
			super(ZaloWorkerConfig.createGmConfig("zgroupmedia"));

			Map<String, Class<? extends IZaloFunction>> functionMap = new HashMap<>();

			functionMap.put(CMDs.GET_GROUPS_FOLLOW_MEMORY + "", GetGroupsFollowMemoryFunc.class);
			functionMap.put(CMDs.SET_GROUPS_FOLLOW_MEMORY + "", SetGroupsFollowMemoryFunc.class);
			functionMap.put(CMDs.GET_RANDOM_MEMORIES + "", RandomMemoriesFunc.class);
			functionMap.put(CMDs.SHARE_MEMORIES + "", ShareMemoryFunc.class);

			setFuncWorkerClassList(functionMap);
		}

	}

	static {
		CustomStats.addHandler("zgroupmedia", workerController);
	}

	protected ZGroupMediaMemoryWorker() {
		Profiler.createThreadProfiler("ZGroupMediaMemoryWorker.init", false);
		Profiler.closeThreadProfiler();
	}

	@Override
	public void run() {
		while (true) {
			try {
				workerController.startWorker();
			} catch (Exception ex) {
				_Logger.error("Can't start worker", ex);
			} finally {
				if (started == false) {
					_Logger.info(startingMsg);
					started = true;
				}
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException ex) {
					Thread.currentThread().interrupt();
				} catch (Exception ex) {
				}
			}
		}
	}
}
