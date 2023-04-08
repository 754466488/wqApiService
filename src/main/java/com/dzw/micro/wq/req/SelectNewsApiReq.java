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
public class SelectNewsApiReq extends PageReq {
	@ApiModelProperty(value = "所属菜单")
	private Integer menuId;
}
