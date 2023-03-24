package com.dzw.micro.wq.application.aspect.handler.api;

import com.dzw.micro.wq.application.aspect.handler.AspectHandler;
import com.dzw.micro.wq.application.aspect.internal.AspectInfo;
import com.dzw.micro.wq.application.aspect.internal.AspectTypeEnum;
import com.dzw.micro.wq.application.domain.constant.ErrorCode;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 接口jsr注解校验拦截器
 *
 * @author zhoutao
 * @date 2020/11/26
 */
@Component
@Order(1)
public class ApiParamHandler implements AspectHandler {
	/**
	 * @See hibernate-validator-{$version}.jar
	 * ValidationMessages.properties ValidationMessages_zh_CN.properties
	 */
	private static final List<String> PARAM_REQUIRED_DEFAULT_MESSAGE_LIST = Lists.newArrayList(
			"must not be blank",
			"must not be empty",
			"must not be null",
			"不能为空",
			"不能为null"
	);
	private static final List<String> PARAM_REQUIRED_CODE_LIST = Lists.newArrayList(
			NotBlank.class.getSimpleName(),
			NotEmpty.class.getSimpleName(),
			NotNull.class.getSimpleName()
	);

	@Override
	public AspectTypeEnum getAspectTypeEnum() {
		return AspectTypeEnum.HTTP_RECEIVE;
	}

	@Override
	public Resp preHandle(AspectInfo aspectInfo) throws Throwable {
		// 从参数列表中提取BindingResult
		BindingResult bindingResult = aspectInfo.findParam(BindingResult.class);
		if (bindingResult == null || !bindingResult.hasErrors()) {
			return Resp.success();
		}

		List<FieldError> fieldErrorList = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrorList) {
			String bindField = fieldError.getField();
			String bindMessage = fieldError.getDefaultMessage();

			if (PARAM_REQUIRED_CODE_LIST.contains(fieldError.getCode())) {
				return Resp.error(ErrorCode.PARAM_REQUIRED.getCode(),
						StringUtils.isNotBlank(bindMessage) && !PARAM_REQUIRED_DEFAULT_MESSAGE_LIST.contains(bindMessage) ?
								bindMessage :
								String.format(ErrorCode.PARAM_REQUIRED.getMessage(), bindField));
			}

			return Resp.error(ErrorCode.PARAM_INVALID.getCode(),
					StringUtils.isNotBlank(bindMessage) ?
							bindMessage :
							String.format(ErrorCode.PARAM_INVALID.getMessage(), bindField));
		}

		return Resp.success();
	}
}
