package com.dzw.micro.wq.application.aspect;

import com.dzw.micro.wq.application.aspect.handler.AspectHandlerChain;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 拦截数据库访问
 *
 * @author lyb
 * @date 2020/11/26
 */
@Aspect
@Component
public class DaoCallAspect {
	@Autowired
	private AspectHandlerChain aspectHandlerChain;

//	@Around("execution(public * com.dzw.micro.wq.mapper.*(..))")
//	private Object aroundHttpClientUtils(ProceedingJoinPoint pjp) throws Throwable {
//		return AspectHelper.nonEntranceMethodAspectAround(pjp, AspectTypeEnum.HTTP_CALL, aspectHandlerChain);
//	}
}
