/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vng.zing.worker.base;

/**
 *
 * @author dungln
 */
public class WorkerHeader {

	public int ver = 1, seqId = 2, srcId = 3, dstId = 4, i5 = 5;
	public short cmd = 6, globalMsgId = 7;
	public byte subCommand = 8;

	public WorkerHeader(short cmd, byte subCommand) {
		this.cmd = cmd;
		this.subCommand = subCommand;
	}
};
