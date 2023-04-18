package com.dzw.micro.wq.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class MenuEntity {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "父级id")
	private Long pid;
	@ApiModelProperty(value = "层级")
	private Integer level;
	@ApiModelProperty(value = "顺序")
	private Integer req;
	@ApiModelProperty(value = "图片地址")
	private String picUrl;
	@ApiModelProperty(value = "状态 0：启用 1：停用")
	private Integer status;
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	@ApiModelProperty(value = "创建人")
	private String createUser;
	@ApiModelProperty(value = "修改时间")
	private String updateTime;
	@ApiModelProperty(value = "修改人")
	private String updateUser;
}