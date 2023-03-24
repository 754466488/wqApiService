package com.dzw.micro.wq.application.aspect.handler;

import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 切面处理器链
 *
 * @author lyb
 * @date 2020/11/26
 */
@Component
public class AspectHandlerChain {
	@Autowired
	private ApplicationContext applicationContext;
	private static final Map<AspectTypeEnum, List<AspectHandler>> HANDLER_MAP = Maps.newConcurrentMap();

	@PostConstruct
	void init() {
		/**
		 * 根据不同的切面类型初始化不同的切面处理器链
		 */
		Map<String, AspectHandler> handlerBeanMap = applicationContext.getBeansOfType(AspectHandler.class);
		for (Map.Entry<String, AspectHandler> entry : handlerBeanMap.entrySet()) {
			AspectHandler aspectHandler = entry.getValue();
			AspectTypeEnum aspectTypeEnum = aspectHandler.getAspectTypeEnum();
			List<AspectHandler> aspectHandlerList = HANDLER_MAP.get(aspectTypeEnum);
			if (aspectHandlerList == null) {
				aspectHandlerList = Lists.newArrayList();
			}
			aspectHandlerList.add(aspectHandler);
			HANDLER_MAP.put(aspectTypeEnum, aspectHandlerList);
		}
		/**
		 * 按照@Order升序
		 */
		for (Map.Entry<AspectTypeEnum, List<AspectHandler>> entry : HANDLER_MAP.entrySet()) {
			List<AspectHandler> aspectHandlerList = entry.getValue();
			Collections.sort(aspectHandlerList, (o1, o2) -> {
				int n1 = o1.getClass().getAnnotation(Order.class).value();
				int n2 = o2.getClass().getAnnotation(Order.class).value();
				return n1 < n2 ? -1 : (n1 > n2 ? 1 : 0);
			});
		}
	}

	public Resp applyPreHandle(AspectInfo aspectInfo) throws Throwable {
		List<AspectHandler> aspectHandlerList = HANDLER_MAP.get(aspectInfo.getAspectTypeEnum());
		int handlerSize = aspectHandlerList.size();
		int loggerHandlerIndex = handlerSize - 1;
		for (int i = 0; i < handlerSize; i++) {
			AspectHandler aspectHandler = aspectHandlerList.get(i);
			Resp resp = aspectHandler.preHandle(aspectInfo);
			if (!resp.hasSuccess()) {
				if (i < loggerHandlerIndex) {
					AspectHandler loggerHandler = aspectHandlerList.get(loggerHandlerIndex);
					loggerHandler.preHandle(aspectInfo);
				}
				return resp;
			}
		}
		return Resp.success();
	}

	public void applyPostHandle(AspectInfo aspectInfo, Object result) {
		List<AspectHandler> aspectHandlerList = HANDLER_MAP.get(aspectInfo.getAspectTypeEnum());
		for (AspectHandler aspectHandler : aspectHandlerList) {
			aspectHandler.postHandle(aspectInfo, result);
		}
	}

	public void applyErrorHandle(AspectInfo aspectInfo, Throwable throwable) {
		List<AspectHandler> aspectHandlerList = HANDLER_MAP.get(aspectInfo.getAspectTypeEnum());
		for (AspectHandler aspectHandler : aspectHandlerList) {
			aspectHandler.errorHandle(aspectInfo, throwable);
		}
	}
}
