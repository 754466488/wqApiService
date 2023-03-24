package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.MixedErrorCode;
import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Description: 调用方验证和分配
 * @Author: lyb
 * @Date: 2023/1/28 5:27 下午
 */
@Component
@Order(2)
public class ApiAppIdHandler implements AspectHandler {
	/**
	 * 事先给调用方分配
	 * key:appId
	 * value:appSecret (md5(appId+"8Mj7ia6"))
	 */
	private static final Map<String, String> APP_ID_MAP = Maps.newHashMap();

	static {
		//给自己分配一个appId
		APP_ID_MAP.put(Application.NAME, "9d23bb2d984ee0039228aa57a925ac5b");
	}

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.HTTP_RECEIVE;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		BaseReq baseReq = aspectInfo.findParam(BaseReq.class);
		if (baseReq == null || StringUtils.isBlank(baseReq.getAppId())) {
			return Resp.error(MixedErrorCode.PARAM_REQUIRED, "appId");
		}
		if (StringUtils.isBlank(APP_ID_MAP.get(baseReq.getAppId()))) {
			return Resp.error(MixedErrorCode.INVALID_CALLER);
		}
		return Resp.success();
	}
}
