package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class SetIsTopReq extends BaseAdminReq {
	@NotNull(message = "id不能为空")
	@ApiModelProperty(value = "id")
	private Long id;
	@NotNull(message = "状态不能为空")
	@ApiModelProperty(value = "是否设置为头条 0:否 1:是")
	private Integer isTop;
}
