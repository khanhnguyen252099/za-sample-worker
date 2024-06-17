/*
 * Copyright (c) 2012-2016 by Zalo Group.
 * All Rights Reserved.
 */
package com.vng.zing.zgroupmediamemoryworker.app;

import com.vng.zing.logger.ZLogger;
import com.vng.zing.zgroupmediamemoryworker.model.GroupMediaDB;
import com.vng.zing.zgroupmediamemoryworker.workers.ZGroupMediaMemoryWorker;

/**
 *
 * @author namnq
 */
public class MainApp {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		ZLogger.getLogger("Init GroupMediaMemoryWorker");
		GroupMediaDB.init();
		ZGroupMediaMemoryWorker runClass = ZGroupMediaMemoryWorker.Instance;
		runClass.start();
	}
}
