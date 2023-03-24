package com.dzw.micro.wq.application.aspect.handler.log;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.SymbolConstant;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.domain.utils.IPUtils;
import com.dzw.micro.wq.application.log.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * mq消费日志打印
 * 日志格式：应用名称|本机IP|mqConsume|consumeClassName.methodName|入参|结果|耗时毫秒
 *
 * @author lyb
 * @date 2020/11/26
 */
@Component
@Order(99)
public class MqConsumeLogger implements AspectHandler {
	@Autowired
	private LoggerHelper loggerHelper;

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.MQ_CONSUME;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		StringBuilder sb = aspectInfo.getLogSB();
		sb.append(Application.NAME).append(SymbolConstant.BAR);
		sb.append(IPUtils.LOCAL_IP).append(SymbolConstant.BAR);
		sb.append(AspectTypeEnum.MQ_CONSUME.getName()).append(SymbolConstant.BAR);
		sb.append(loggerHelper.fetchName(aspectInfo.getClassType().getSimpleName(), aspectInfo.getMethodName())).append(SymbolConstant.BAR);
		sb.append(loggerHelper.fetchParam(aspectInfo.getMethodParam())).append(SymbolConstant.BAR);
		return Resp.success();
	}

	@Override
	public void postHandle(AspectInfo aspectInfo, Object result) {
		StringBuilder sb = aspectInfo.getLogSB();
		sb.append(loggerHelper.fetchResult(result)).append(SymbolConstant.BAR);
		sb.append(System.currentTimeMillis() - aspectInfo.getCreateTime()).append(SymbolConstant.BAR);
		Log.info(sb.toString());
	}

	@Override
	public void errorHandle(AspectInfo aspectInfo, Throwable throwable) {
		StringBuilder errorSB = new StringBuilder();
		errorSB.append(loggerHelper.fetchName(aspectInfo.getClassType().getSimpleName(), aspectInfo.getMethodName())).append(SymbolConstant.BAR);
		Log.error(errorSB.toString(), throwable);
	}
}
