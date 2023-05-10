package com.dzw.micro.wq.application.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * api基础请求对象
 * 封装系统级参数,具体的校验可以放到ApiInterceptor中这里不做强制JSR注解校验
 *
 * @author lyb
 * @date 2020/11/26
 */
@Data
@ApiModel
public class BaseAdminReq implements Req {
	@ApiModelProperty(value = "用户id")
	private Long staffId;
	@ApiModelProperty(value = "用户名")
	private String userName;
}
