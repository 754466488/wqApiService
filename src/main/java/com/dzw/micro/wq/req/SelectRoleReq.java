package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class SelectRoleReq extends AdminPageReq{
	@ApiModelProperty(value = "角色名称")
	private String roleName;
	@ApiModelProperty(value = "创建人")
	private String createUser;
}
