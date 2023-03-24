package com.dzw.micro.wq.application.domain.utils;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.reflect.Field;

public class SpringContentUtils implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		applicationContext = context;
	}

	/**
	 * 获取被代理对象
	 *
	 * @param className
	 * @return
	 * @throws RuntimeException
	 */
	public static Object getBean(String className) throws RuntimeException {
		try {
			Class clss = Class.forName(className);
			return applicationContext.getBean(clss);
		} catch (Exception e) {
			throw new RuntimeException("无法从容器加载类[" + className + "][" + e.getMessage() + "]");
		}
	}

	/**
	 * 获取被代理对象
	 *
	 * @param className
	 * @param superClass
	 * @param <T>
	 * @return
	 * @throws RuntimeException
	 */
	public static <T> T getBean(String className, Class<T> superClass) throws RuntimeException {
		try {
			Class clss = Class.forName(className);
			Object object = applicationContext.getBean(clss);
			if (!superClass.isAssignableFrom(object.getClass())) {
				throw new RuntimeException("该类必须继承或实现[" + superClass.getName() + "]");
			}
			return superClass.cast(object);
		} catch (Exception e) {
			throw new RuntimeException("无法从容器加载类[" + className + "][" + e.getMessage() + "]");
		}
	}

	/**
	 * 获取原始对象
	 *
	 * @param className
	 * @return
	 * @throws RuntimeException
	 */
	public static Object getTarget(String className) throws RuntimeException {
		Object proxy = getBean(className);

		if (!AopUtils.isAopProxy(proxy)) {
			return proxy;
		}

		try {
			if (AopUtils.isJdkDynamicProxy(proxy)) {
				return getJdkDynamicProxyTargetObject(proxy);
			}
			if (AopUtils.isCglibProxy(proxy)) {
				return getCglibProxyTargetObject(proxy);
			}

			return null;
		} catch (Exception e) {
			throw new RuntimeException("无法从容器加载原始类[" + className + "][" + e.getMessage() + "]");
		}
	}

	private static Object getCglibProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
		h.setAccessible(true);
		Object dynamicAdvisedInterceptor = h.get(proxy);

		Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();
		return target;
	}

	private static Object getJdkDynamicProxyTargetObject(Object proxy) throws Exception {
		Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
		h.setAccessible(true);
		AopProxy aopProxy = (AopProxy) h.get(proxy);

		Field advised = aopProxy.getClass().getDeclaredField("advised");
		advised.setAccessible(true);

		Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();
		return target;
	}
}
