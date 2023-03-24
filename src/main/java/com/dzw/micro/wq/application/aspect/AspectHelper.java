package com.dzw.micro.wq.application.aspect;

import com.dzw.micro.wq.application.aspect.handler.AspectHandlerChain;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectInfoFactory;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.req.Resp;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * AspectHelper
 *
 * @author lyb
 * @date 2022/4/1
 */
public class AspectHelper {
	private AspectHelper() {
	}

	/**
	 * dao、httpCall等非入口切面拦截方法通用处理逻辑
	 *
	 * @return
	 */
	static Object nonEntranceMethodAspectAround(ProceedingJoinPoint pjp,
												AspectTypeEnum aspectTypeEnum,
												AspectHandlerChain aspectHandlerChain) throws Throwable {
		AspectInfo aspectInfo = AspectInfoFactory.create(pjp, aspectTypeEnum);
		Object result = null;
		try {
			Resp resp = aspectHandlerChain.applyPreHandle(aspectInfo);
			if (resp.hasSuccess()) {
				result = pjp.proceed();
			} else {
				result = resp;
			}
		} catch (Throwable ex) {
			result = "error:" + ex.getMessage();
			throw ex;
		} finally {
			aspectHandlerChain.applyPostHandle(aspectInfo, result);
		}
		return result;
	}
}
