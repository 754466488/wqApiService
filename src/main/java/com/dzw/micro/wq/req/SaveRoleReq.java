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
public class SaveRoleReq extends BaseAdminReq {
	private Long roleId;
	@NotBlank(message = "角色名称不能为空")
	@ApiModelProperty(value = "角色名称")
	private String roleName;
	private String roleDesc;
}
