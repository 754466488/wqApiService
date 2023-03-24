package com.dzw.micro.wq.application.thread;

import java.util.Locale;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 带名称前缀的线程工厂
 *
 * @Author zhoutao
 * @Date 2017/7/4
 */
public class NamedThreadFactory implements ThreadFactory {
	public static final String COMMON_PREFIX = "pool-";
	private static final AtomicInteger threadPoolNumber = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	private static final String NAME_PATTERN = "%s-%d-thread";
	private final String threadNamePrefix;
	private final boolean isDaemon;
	private final Thread.UncaughtExceptionHandler handler;

	public NamedThreadFactory(String threadNamePrefix) {
		this(threadNamePrefix, false, null);
	}

	public NamedThreadFactory(String threadNamePrefix, boolean isDaemon) {
		this(threadNamePrefix, isDaemon, null);
	}

	public NamedThreadFactory(String threadNamePrefix, boolean isDaemon, Thread.UncaughtExceptionHandler handler) {
		SecurityManager s = System.getSecurityManager();
		this.group = s != null ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		this.threadNamePrefix = String.format(Locale.ROOT, NAME_PATTERN, new Object[]{checkPrefix(threadNamePrefix), Integer.valueOf(threadPoolNumber.getAndIncrement())});
		this.isDaemon = isDaemon;
		this.handler = handler;
	}

	public static NamedThreadFactory newNamedThreadFactory(String threadNamePrefix) {
		return new NamedThreadFactory(COMMON_PREFIX + threadNamePrefix, false, null);
	}

	public static NamedThreadFactory newNamedThreadFactory(String threadNamePrefix, boolean isDaemon) {
		return new NamedThreadFactory(COMMON_PREFIX + threadNamePrefix, isDaemon, null);
	}

	public static NamedThreadFactory newNamedThreadFactory(String threadNamePrefix, boolean isDaemon, Thread.UncaughtExceptionHandler handler) {
		return new NamedThreadFactory(COMMON_PREFIX + threadNamePrefix, isDaemon, handler);
	}

	private static String checkPrefix(String prefix) {
		return prefix != null && prefix.length() != 0 ? prefix : "NamedPool";
	}

	@Override
	public Thread newThread(Runnable r) {
		String threadName = String.format(Locale.ROOT, "%s-%d", this.threadNamePrefix, Integer.valueOf(this.threadNumber.getAndIncrement()));
		Thread t = new Thread(this.group, r, threadName, 0L);
		t.setDaemon(this.isDaemon);
		if (null != this.handler) {
			t.setUncaughtExceptionHandler(this.handler);
		}
		t.setPriority(5);
		return t;
	}
}
