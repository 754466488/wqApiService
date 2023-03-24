package com.dzw.micro.wq.application.aspect.internal;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 切面信息工厂类
 *
 * @author lyb
 * @date 2020/11/26
 */
public class AspectInfoFactory {
	public static final AspectInfo create(ProceedingJoinPoint pjp, AspectTypeEnum aspectTypeEnum) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

		AspectInfo aspectInfo = new AspectInfo();
		aspectInfo.setMethodParam(pjp.getArgs());
		aspectInfo.setMethodName(methodSignature.getName());
		aspectInfo.setMethodAnnotations(methodSignature.getMethod().getAnnotations());
		aspectInfo.setClassType(pjp.getTarget().getClass());
		aspectInfo.setAspectTypeEnum(aspectTypeEnum);
		aspectInfo.setCreateTime(System.currentTimeMillis());
		aspectInfo.setLogSB(new StringBuilder());
		return aspectInfo;
	}

	public static final AspectInfo create(ProceedingJoinPoint pjp) {
		MethodSignature methodSignature = (MethodSignature) pjp.getSignature();

		AspectInfo aspectInfo = new AspectInfo();
		aspectInfo.setMethodParam(pjp.getArgs());
		aspectInfo.setMethodName(methodSignature.getName());
		aspectInfo.setMethodAnnotations(methodSignature.getMethod().getAnnotations());
		aspectInfo.setClassType(pjp.getTarget().getClass());
		return aspectInfo;
	}
}
