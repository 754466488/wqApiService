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
public class SelectRoleReq extends PageReq{
	@ApiModelProperty(value = "角色名称")
	private String roleName;
}
