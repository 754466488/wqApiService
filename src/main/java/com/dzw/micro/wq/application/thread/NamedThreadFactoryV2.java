package com.dzw.micro.wq.application.thread;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带名称前缀的线程工厂V2版本
 *
 * @Author zhoutao
 * @Date 2017/7/4
 */
public class NamedThreadFactoryV2 implements ThreadFactory {
	public static final String COMMON_PREFIX = "pool-";
	private static final AtomicInteger threadPoolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private static final String NAME_PATTERN = "%s-%d-thread";
	private final String threadNamePrefix;
	private final boolean isDaemon;
	private final Thread.UncaughtExceptionHandler handler;

	private NamedThreadFactoryV2(String threadNamePrefix, boolean isDaemon) {
		SecurityManager s = System.getSecurityManager();
		this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.threadNamePrefix = String.format(Locale.ROOT, NAME_PATTERN, new Object[]{checkPrefix(threadNamePrefix), Integer.valueOf(threadPoolNumber.getAndIncrement())});
		this.isDaemon = isDaemon;
		this.handler = new UncaughtExceptionLogger();
	}

	public static NamedThreadFactory newInstance(String threadNamePrefix) {
		return new NamedThreadFactory(COMMON_PREFIX + threadNamePrefix, false);
	}

	public static NamedThreadFactory newInstance(String threadNamePrefix, boolean isDaemon) {
		return new NamedThreadFactory(COMMON_PREFIX + threadNamePrefix, isDaemon);
	}

	private static String checkPrefix(String prefix) {
		return prefix != null && prefix.length() != 0 ? prefix : "NamedPool";
	}

	@Override
	public Thread newThread(Runnable r) {
		Runnable runnable = RunnableWrapper.of(r);
		String threadName = String.format(Locale.ROOT, "%s-%d", this.threadNamePrefix, Integer.valueOf(this.threadNumber.getAndIncrement()));
		Thread t = new Thread(this.group, runnable, threadName, 0L);
		t.setDaemon(this.isDaemon);
		t.setUncaughtExceptionHandler(this.handler);
		t.setPriority(5);
		return t;
	}
}
