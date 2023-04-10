package com.dzw.micro.wq.application.config.concurrent;

import com.dzw.micro.wq.application.log.Log;
import com.dzw.micro.wq.application.thread.NamedThreadFactory;
import com.dzw.micro.wq.application.thread.RunnableWrapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池配置
 *
 * @author lyb
 * @date 2021/9/2
 */
@Configuration
public class ThreadPoolConfig {
	public static final int CPU_CORE_NUMBER = Runtime.getRuntime().availableProcessors();

	/**
	 * 1、该异常Handler只作用于直接使用 ThreadPoolExecutor.execute方法提交的任务
	 * 2、ThreadPoolExecutor.submit 提交的任务运行抛出的异常需要通过返回值 Future获取
	 * 3、而使用spring注解@async的任务会自动被spring体系下的AsyncUncaughtExceptionHandler捕获并打印
	 *
	 * @return - Thread.UncaughtExceptionHandler
	 */
	@Bean(name = "uncaughtExceptionHandler")
	public Thread.UncaughtExceptionHandler uncaughtExceptionHandler() {
		Thread.UncaughtExceptionHandler uncaughtExceptionHandler = (t, e) -> {
			Log.error("catch a uncaughtException", e);
		};
		return uncaughtExceptionHandler;
	}

	/**
	 * 业务模块通用线程池
	 * 具体业务根据情况自定义自己的线程池
	 *
	 * @param uncaughtExceptionHandler
	 * @return - ThreadPoolExecutor
	 */
	@Bean(name = "commonExecutor")
	public ThreadPoolExecutor commonExecutor(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
		return new ThreadPoolExecutor(CPU_CORE_NUMBER, CPU_CORE_NUMBER * 2,
				1, TimeUnit.MINUTES,
				new ArrayBlockingQueue<>(500),
				new NamedThreadFactory("commonExecutor", true, uncaughtExceptionHandler) {
					@Override
					public Thread newThread(Runnable r) {
						return super.newThread(RunnableWrapper.of(r));
					}
				},
				new ThreadPoolExecutor.CallerRunsPolicy());
	}

	/**
	 * 系统内部使用的线程池
	 * 业务模块不要使用
	 *
	 * @param uncaughtExceptionHandler
	 * @return - ThreadPoolExecutor
	 */
	@Bean(name = "internalExecutor")
	public ThreadPoolExecutor internalExecutor(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
		return new ThreadPoolExecutor(CPU_CORE_NUMBER, CPU_CORE_NUMBER,
				1, TimeUnit.MINUTES,
				new ArrayBlockingQueue<>(1),
				new NamedThreadFactory("internalExecutor", false, uncaughtExceptionHandler) {
					@Override
					public Thread newThread(Runnable r) {
						return super.newThread(RunnableWrapper.of(r));
					}
				},
				new ThreadPoolExecutor.CallerRunsPolicy());
	}
}
