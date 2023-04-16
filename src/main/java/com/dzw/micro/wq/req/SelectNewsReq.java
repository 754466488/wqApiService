package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class SelectNewsReq extends PageReq {
	@ApiModelProperty(value = "状态 0：启用 1：停用  2：待发布")
	private Integer status;
	@ApiModelProperty(value = "开始时间")
	private String beginTime;
	@ApiModelProperty(value = "结束时间")
	private String endTime;
	@ApiModelProperty(value = "所属菜单")
	private Integer menuId;
	@ApiModelProperty(value = "是否设置为头条 0:否 1:是")
	private Integer isTop;
	private Integer isGw;
}
