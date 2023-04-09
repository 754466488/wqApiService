package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author lyb
 * @version 1.0
 * @Description: 下拉选
 * @date 2021/1/611:33 下午
 */
@Data
@ApiModel
public class SelectedResp {
	@ApiModelProperty(value = "code")
	private int code;
	@ApiModelProperty(value = "名称")
	private String name;
}
