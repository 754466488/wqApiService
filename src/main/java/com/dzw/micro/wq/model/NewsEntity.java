package com.dzw.micro.wq.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class NewsEntity {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "所属菜单")
	private Long menuId;
	@ApiModelProperty(value = "所属菜单名称")
	private String menuName;
	@ApiModelProperty(value = "点击量")
	private Integer clickNum;
	@ApiModelProperty(value = "外链文章地址")
	private String linkUrl;
	@ApiModelProperty(value = "文章封面图片地址")
	private String coverPicUrl;
	@ApiModelProperty(value = "文章内容")
	private String content;
	@ApiModelProperty(value = "是否设置为头条 0:否 1:是")
	private Integer isTop;
	@ApiModelProperty(value = "类型 0：普通 1：外链文章")
	private Integer type;
	@ApiModelProperty(value = "状态 0：启用 1：停用  2：待发布")
	private Integer status;
	@ApiModelProperty(value = "发布时间")
	private String publishTime;
	@ApiModelProperty(value = "创建时间")
	private String createTime;
	@ApiModelProperty(value = "创建人")
	private String createUser;
	@ApiModelProperty(value = "更新时间")
	private String updateTime;
	@ApiModelProperty(value = "更新人")
	private String updateUser;
}