package com.dzw.micro.wq.application.thread;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhoutao
 * @date 2022/12/7
 */
@Slf4j
public class CallerPutQueuePolicy implements RejectedExecutionHandler {
	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		try {
			executor.getQueue().put(r);
		} catch (InterruptedException e) {
			log.error("CallerPutQueuePolicy.rejectedExecution error", e);
		}
	}
}
