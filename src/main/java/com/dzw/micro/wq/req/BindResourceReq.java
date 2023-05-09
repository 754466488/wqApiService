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
public class BindResourceReq extends BaseAdminReq {
	@ApiModelProperty(value = "角色id")
	private long roleId;
	@NotBlank(message = "菜单id不能为空")
	@ApiModelProperty(value = "菜单id，多个逗号分隔")
	private String resourceIds;
}
