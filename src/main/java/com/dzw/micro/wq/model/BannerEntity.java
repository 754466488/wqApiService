package com.dzw.micro.wq.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class BannerEntity {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "图片地址")
	private String url;
	@ApiModelProperty(value = "轮播图位置")
	private Integer req;
	@ApiModelProperty(value = "状态0：启用 1：停用")
	private Integer status;
	@ApiModelProperty(value = "链接地址")
	private String linkUrl;
	@ApiModelProperty(value = "类型")
	private Integer type;
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	@ApiModelProperty(value = "创建人")
	private String createUser;
	@ApiModelProperty(value = "修改时间")
	private String updateTime;
	@ApiModelProperty(value = "修改人")
	private String updateUser;
}