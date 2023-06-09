package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Data
public class MenuTreeResp {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "子菜单集合")
	List<MenuTreeResp> childMenuList;
}
