package com.dzw.micro.wq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.util.StringUtils;


/**
 * @Description: 启动类
 * @Author: lyb
 * @Date: 2023/1/28 5:05 下午
 */
@EnableCaching
@EnableScheduling
@EnableAsync
@SpringBootApplication(scanBasePackages = {"com.dzw.micro.wq"})
@EnableAspectJAutoProxy(proxyTargetClass = true, exposeProxy = true)
public class Application {
	public static final String NAME = System.getProperty("spring.application.name");
	public static final String ENV = System.getProperty("spring.profiles.active");

	public static void main(String[] args) throws Exception {
		checkOnStartUp();
		SpringApplication.run(Application.class, args);
	}

	/**
	 * 检查启动必须的环境变量是否设置
	 *
	 * @throws Exception
	 */
	private static void checkOnStartUp() throws Exception {
		if (StringUtils.isEmpty(NAME)) {
			throw new Exception("Start application, must set environment variable:spring.application.name");
		}
		if (StringUtils.isEmpty(ENV)) {
			throw new Exception("Start application, must set environment variable:spring.profiles.active");
		}
	}


	public static boolean isDev() {
		return "dev".equals(ENV);
	}

	public static boolean isTest() {
		return "test".equals(ENV);
	}

	public static boolean isStage() {
		return "stage".equals(ENV);
	}

	public static boolean isProd() {
		return "prod".equals(ENV);
	}

}
