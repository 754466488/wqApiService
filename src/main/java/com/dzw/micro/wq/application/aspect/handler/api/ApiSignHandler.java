package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.ErrorCode;
import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.domain.utils.WebUtils;
import com.dzw.micro.wq.application.utils.MD5Utils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description: 接口签名验证
 * @Author: lyb
 * @Date: 2023/1/28 5:29 下午
 */
@Component
@Order(3)
public class ApiSignHandler implements AspectHandler {
	private static final String APP_ID = "2BYdFOItTRXtQmFL9cY6pgEDLhXG1JwX5P";

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.HTTP_RECEIVE;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		if (Application.isDev()) {
			return Resp.success();
		}
		String requestURI = WebUtils.getRequestURI();
		Object[] methodParam = aspectInfo.getMethodParam();

		/**
		 * 校验api下接口
		 */
		if (StringUtils.startsWithAny(requestURI, "/api/")) {
			BaseReq baseReq = getBaseReqFromApiParams(methodParam);
			String timestamp = baseReq.getTimestamp();
			String sign = baseReq.getSign();
			String signNew = MD5Utils.md5(APP_ID + timestamp).toLowerCase();
			if (!Objects.equals(sign, signNew)) {
				return Resp.error(ErrorCode.SIGN);
			}
			return Resp.success();
		}
		return Resp.success();
	}

	public static final BaseReq getBaseReqFromApiParams(Object[] params) {
		if (ArrayUtils.isNotEmpty(params)) {
			for (Object param : params) {
				if (param instanceof BaseReq) {
					BaseReq baseReq = (BaseReq) param;
					return baseReq;
				}
			}
		}
		return null;
	}
}
