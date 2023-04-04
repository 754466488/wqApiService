package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 轮播图列表
 *
 * @author lyb
 * @date created in 2023/4/4
 */
@Data
public class BannerListResp {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "标题")
	private String title;
	@ApiModelProperty(value = "图片地址")
	private String url;
	@ApiModelProperty(value = "轮播图位置")
	private int req;
	@ApiModelProperty(value = "状态0：启用 1：停用")
	private String status;
	@ApiModelProperty(value = "链接地址")
	private String linkUrl;
	@ApiModelProperty(value = "类型")
	private String type;
	@ApiModelProperty(value = "创建时间")
	private String createTime;
}
