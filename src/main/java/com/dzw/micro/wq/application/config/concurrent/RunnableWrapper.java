package com.dzw.micro.wq.application.config.concurrent;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Runnable包装类
 *
 * @author lyb
 * @date 2021/9/2
 */
public class RunnableWrapper implements Runnable {
	private Map<String, String> contextMap;
	private Runnable runnable;
	private Thread mainThread;

	public RunnableWrapper(Runnable runnable) {
		Map<String, String> mdcMap = MDC.getCopyOfContextMap();
		this.contextMap = mdcMap == null ? new HashMap<>(1) : mdcMap;
		this.runnable = runnable;
		this.mainThread = Thread.currentThread();
	}

	public static RunnableWrapper of(Runnable runnable) {
		if (runnable instanceof RunnableWrapper) {
			return (RunnableWrapper) runnable;
		}
		return new RunnableWrapper(runnable);
	}

	@Override
	public void run() {
		if (Objects.equals(mainThread, Thread.currentThread())) {
			mainRun();
		} else {
			workerRun();
		}
	}

	/**
	 * 主线程执行
	 */
	private void mainRun() {
		runnable.run();
	}

	/**
	 * 线程池中的工作线程执行
	 */
	private void workerRun() {
		contextMap.forEach(MDC::put);
		try {
			runnable.run();
		} finally {
			MDC.clear();
			contextMap.clear();
			contextMap = null;
			runnable = null;
			mainThread = null;
		}
	}
}
