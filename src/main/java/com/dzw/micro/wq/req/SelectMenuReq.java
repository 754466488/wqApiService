package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Data
public class SelectMenuReq extends PageReq {
	@NotNull(message = "父级id不能为空")
	@ApiModelProperty(value = "父级id")
	private Long pid;
}
