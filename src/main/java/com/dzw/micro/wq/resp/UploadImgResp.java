package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/5 23:56
 */
@Data
@ApiModel
public class UploadImgResp {
	@ApiModelProperty(value = "图片链接")
	private String url;
}
