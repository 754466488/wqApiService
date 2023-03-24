package com.dzw.micro.wq.application.aspect.internal;

import lombok.Getter;

/**
 * 切面类型枚举
 *
 * @author lyb
 * @date 2020/11/26
 */
public enum AspectTypeEnum {
	JOB_RUN("jobRun"),
	HTTP_RECEIVE("httpReceive"),
	DAO_CALL("daoCall"),
	HTTP_CALL("httpCall"),
	CAFFEINE_CALL("caffeineCall"),
	MQ_CONSUME("mqConsume"),
	MQ_PRODUCE("mqProduce"),
	REDIS_CALL("redisCall");

	AspectTypeEnum(String name) {
		this.name = name;
	}

	@Getter
	private String name;
}
