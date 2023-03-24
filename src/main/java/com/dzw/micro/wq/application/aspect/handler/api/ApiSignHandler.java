package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.req.Resp;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @Description: 接口签名验证
 * @Author: lyb
 * @Date: 2023/1/28 5:29 下午
 */
@Component
@Order(3)
public class ApiSignHandler implements AspectHandler {

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.HTTP_RECEIVE;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		return Resp.success();
	}
}
