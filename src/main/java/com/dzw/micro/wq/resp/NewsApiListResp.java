package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class NewsApiListResp {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "点击量")
	private Integer clickNum;
	@ApiModelProperty(value = "所属菜单id")
	private Long menuId;
	@ApiModelProperty(value = "所属菜单名称")
	private String menuName;
	@ApiModelProperty(value = "文章内容")
	private String content;
	@ApiModelProperty(value = "发布时间")
	private String publishTime;
	@ApiModelProperty(value = "是否设置为头条 0:否 1:是")
	private Integer isTop;
	@ApiModelProperty(value = "类型 0：普通 1：外链文章")
	private Integer type;
	@ApiModelProperty(value = "外链文章地址")
	private String linkUrl;
	@ApiModelProperty(value = "文章封面图片地址")
	private String coverPicUrl;
}
