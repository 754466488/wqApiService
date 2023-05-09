package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
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
public class BindRoleReq extends BaseAdminReq {
	@ApiModelProperty(value = "绑定用户id")
	private long bindStaffId;
	@NotBlank(message = "角色id不能为空")
	@ApiModelProperty(value = "角色id，多个逗号隔开")
	private String roleIds;
}
