package com.dzw.micro.wq.application.aspect;

import com.dzw.micro.wq.application.aspect.handler.AspectHandlerChain;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectInfoFactory;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.MixedConstant;
import com.dzw.micro.wq.application.domain.id.DistributedId;
import com.dzw.micro.wq.application.domain.req.Resp;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description: Job的统一拦截
 * @Author: lyb
 * @Date: 2023/1/28 5:37 下午
 */
@Aspect
@Component
public class JobRunAspect {
	@Autowired
	private AspectHandlerChain aspectHandlerChain;

//	@Around("execution(public * com.dzw.micro.wq..*Job.execute(..))")
//	public void aroundElasticSimpleJob(ProceedingJoinPoint pjp) throws Throwable {
//		around(pjp);
//	}
//
//	@Around("@annotation(org.springframework.scheduling.annotation.Scheduled) && within(com.dzw..*)")
//	public void aroundSpringAnnotationJob(ProceedingJoinPoint pjp) throws Throwable {
//		around(pjp);
//	}

	private void around(ProceedingJoinPoint pjp) throws Throwable {
		MDC.put(MixedConstant.TRACE_ID, DistributedId.get().toString());

		AspectInfo aspectInfo = AspectInfoFactory.create(pjp, AspectTypeEnum.JOB_RUN);
		String result = null;
		try {
			Resp resp = aspectHandlerChain.applyPreHandle(aspectInfo);
			if (resp.hasSuccess()) {
				pjp.proceed();
			}
			result = resp.getMessage();
		} catch (Throwable ex) {
			result = "error:" + ex.getMessage();
			aspectHandlerChain.applyErrorHandle(aspectInfo, ex);
		} finally {
			aspectHandlerChain.applyPostHandle(aspectInfo, result);

			MDC.remove(MixedConstant.TRACE_ID);
		}
	}
}
