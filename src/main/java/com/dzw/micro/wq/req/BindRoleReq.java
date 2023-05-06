package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class BindRoleReq {
	@NotBlank(message = "用户id不能为空")
	@ApiModelProperty(value = "用户id")
	private long staffId;
	@NotBlank(message = "角色id不能为空")
	@ApiModelProperty(value = "角色id，多个逗号隔开")
	private String roleIds;
	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名")
	private String userName;
}
