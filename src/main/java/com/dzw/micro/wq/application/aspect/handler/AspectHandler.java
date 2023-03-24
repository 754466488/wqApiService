package com.dzw.micro.wq.application.aspect.handler;


import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.req.Resp;

/**
 * @Description: 切面处理器接口
 * @Author: lyb
 * @Date: 2023/1/28 5:30 下午
 */
public interface AspectHandler {

	/**
	 * 要处理的切面类型
	 *
	 * @return
	 */
	AspectTypeEnum getAspectTypeEnum();

	/**
	 * 请求处理之前的处理
	 *
	 * @param aspectInfo
	 * @return - 默认返回Resp.success
	 * @throws Exception
	 */
	default Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		return Resp.success();
	}

	/**
	 * 请求之后的处理
	 *
	 * @param aspectInfo
	 * @param result
	 * @return - 可以做返回值改写
	 * @throws Exception
	 */
	default void postHandle(AspectInfo aspectInfo, Object result) {
	}

	/**
	 * 异常的处理
	 *
	 * @param aspectInfo
	 * @param throwable
	 * @throws Exception
	 */
	default void errorHandle(AspectInfo aspectInfo, Throwable throwable) {
	}
}
