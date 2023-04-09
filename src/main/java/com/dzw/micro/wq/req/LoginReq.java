package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
@Data
public class LoginReq {
	@ApiModelProperty(value = "用户名")
	@NotBlank(message = "用户名不能为空")
	private String userName;
	@NotBlank(message = "密码不能为空")
	@ApiModelProperty(value = "密码")
	private String password;
}
