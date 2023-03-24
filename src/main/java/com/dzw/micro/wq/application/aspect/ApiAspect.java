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
 * 拦截接口
 *
 * @author lyb
 * @date 2020/11/26
 */
@Aspect
@Component
public class ApiAspect {
	@Autowired
	private AspectHandlerChain aspectHandlerChain;

	@Around("@within(org.springframework.web.bind.annotation.RestController) && within(com.dzw.micro.*)")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		MDC.put(MixedConstant.TRACE_ID, DistributedId.get().toString());

		AspectInfo aspectInfo = AspectInfoFactory.create(pjp, AspectTypeEnum.HTTP_RECEIVE);
		Object result = null;
		try {
			Resp resp = aspectHandlerChain.applyPreHandle(aspectInfo);
			if (resp.hasSuccess()) {
				result = pjp.proceed();
			} else {
				result = resp;
			}
		} catch (Throwable ex) {
			aspectHandlerChain.applyErrorHandle(aspectInfo, ex);
			result = Resp.error(ex);
		} finally {
			aspectHandlerChain.applyPostHandle(aspectInfo, result);

			MDC.remove(MixedConstant.TRACE_ID);
		}
		return result;
	}
}