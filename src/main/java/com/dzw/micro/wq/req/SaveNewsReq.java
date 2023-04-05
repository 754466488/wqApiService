package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 保存新闻文章
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class SaveNewsReq {
	@ApiModelProperty(value = "id")
	private Long id;
	@NotBlank(message = "标题不能为空")
	@ApiModelProperty(value = "标题")
	private String title;
	@NotNull(message = "所属菜单不能为空")
	@ApiModelProperty(value = "所属菜单")
	private Long menuId;
	@NotBlank(message = "文章封面图片不能为空")
	@ApiModelProperty(value = "文章封面图片地址")
	private String coverPicUrl;
	@NotBlank(message = "文章内容不能为空")
	@ApiModelProperty(value = "文章内容")
	private String content;
	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名")
	private String userName;
}
