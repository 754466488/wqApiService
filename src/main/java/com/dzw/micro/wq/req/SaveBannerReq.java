package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 添加轮播图
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class SaveBannerReq {
	@ApiModelProperty(value = "id")
	private Long id;
	@NotBlank(message = "标题不能为空")
	@ApiModelProperty(value = "标题")
	private String title;
	@NotBlank(message = "图片地址不能为空")
	@ApiModelProperty(value = "图片地址")
	private String url;
	@ApiModelProperty(value = "链接地址")
	private String linkUrl;
	@NotNull(message = "类型（位置）不能为空")
	@ApiModelProperty(value = "类型（位置）")
	private Integer type;
	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名")
	private String userName;
}
