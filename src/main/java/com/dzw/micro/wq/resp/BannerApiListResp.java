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
public class BannerApiListResp {
	@ApiModelProperty(value = "图片地址")
	private String url;
	@ApiModelProperty(value = "链接地址")
	private String linkUrl;
	@ApiModelProperty(value = "类型")
	private String type;
}
