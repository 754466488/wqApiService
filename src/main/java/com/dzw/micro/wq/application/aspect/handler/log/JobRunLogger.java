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
 * job运行日志打印
 * 日志格式：应用名称|本机IP|jobCall|jobClassName.jobMethodName|入参|结果|耗时毫秒
 *
 * @author lyb
 * @date 2020/11/26
 */
@Component
@Order(99)
public class JobRunLogger implements AspectHandler {
	@Autowired
	private LoggerHelper loggerHelper;

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.JOB_RUN;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		StringBuilder sb = aspectInfo.getLogSB();
		sb.append(Application.NAME).append(SymbolConstant.BAR);
		sb.append(IPUtils.LOCAL_IP).append(SymbolConstant.BAR);
		sb.append(AspectTypeEnum.JOB_RUN.getName()).append(SymbolConstant.BAR);
		sb.append(loggerHelper.fetchName(aspectInfo.getClassType().getSimpleName(), aspectInfo.getMethodName())).append(SymbolConstant.BAR);
		sb.append(loggerHelper.fetchParam(aspectInfo.getMethodParam())).append(SymbolConstant.BAR);
		if (Application.isDev()) {
//			return Resp.error("开发环境不执行直接退出");
		}
		if (Application.isStage()) {
			return Resp.error("预发布环境不执行直接退出");
		}

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
