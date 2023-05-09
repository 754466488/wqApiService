package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.ErrorCode;
import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import com.dzw.micro.wq.application.domain.req.BaseApiReq;
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

		if (Application.isDev()) {
			return Resp.success();
		}

		if (StringUtils.startsWithAny(requestURI, "/admin/sysStaff/login")) {
			return Resp.success();
		}

//		if (StringUtils.startsWithAny(requestURI, "/admin/")) {
//			BaseAdminReq baseApiReq = getBaseAdminReqFromParams(methodParam);
//			if (Objects.isNull(baseApiReq)) {
//				return Resp.error(ErrorCode.AUTHORITY);
//			}
//
//			if (Objects.isNull(baseApiReq.getStaffId())) {
//				return Resp.error("用户id不能为空");
//			}
//			return Resp.success();
//		}

		/**
		 * 校验api下接口
		 */
		if (StringUtils.startsWithAny(requestURI, "/api/")) {
			BaseApiReq baseApiReq = getBaseApiReqFromParams(methodParam);
			if (Objects.isNull(baseApiReq)) {
				return Resp.error(ErrorCode.SIGN);
			}
			String timestamp = baseApiReq.getTimestamp();
			String sign = baseApiReq.getSign().toLowerCase();
			String signNew = MD5Utils.md5(APP_ID + timestamp).toLowerCase();
			if (!Objects.equals(sign, signNew)) {
				return Resp.error(ErrorCode.SIGN);
			}
			return Resp.success();
		}
		return Resp.success();
	}

	public static final BaseApiReq getBaseApiReqFromParams(Object[] params) {
		if (ArrayUtils.isNotEmpty(params)) {
			for (Object param : params) {
				if (param instanceof BaseApiReq) {
					BaseApiReq baseApiReq = (BaseApiReq) param;
					return baseApiReq;
				}
			}
		}
		return null;
	}

	public static final BaseAdminReq getBaseAdminReqFromParams(Object[] params) {
		if (ArrayUtils.isNotEmpty(params)) {
			for (Object param : params) {
				if (param instanceof BaseAdminReq) {
					BaseAdminReq baseAdminReq = (BaseAdminReq) param;
					return baseAdminReq;
				}
			}
		}
		return null;
	}
}
