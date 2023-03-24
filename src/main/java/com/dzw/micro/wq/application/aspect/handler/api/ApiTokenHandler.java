package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.domain.utils.WebUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 接口token验证
 *
 * @author zhoutao
 * @date 2020/11/26
 */
@Component
@Order(4)
public class ApiTokenHandler implements AspectHandler {

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.HTTP_RECEIVE;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		String requestURI = WebUtils.getRequestURI();
		return Resp.success();
	}
}
