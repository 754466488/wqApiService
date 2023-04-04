package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/4
 */
@Data
public class SelectBannerReq extends PageReq {
	@ApiModelProperty(value = "状态0：启用 1：停用")
	private Integer status;
	@ApiModelProperty(value = "类型")
	private Integer type;
}
