package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class ResourceResp {
	@ApiModelProperty(value = "资源id")
	private long resourceId;
	@ApiModelProperty(value = "资源父id")
	private long pid;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "地址")
	private String url;
	@ApiModelProperty(value = "图标")
	private String icon;
	@ApiModelProperty(value = "序号")
	private int req;
	@ApiModelProperty(value = "资源类型：0一级菜单1二级菜单2按钮")
	private int type;
}
