package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Data
public class LeftMenuTreeResp {
	@ApiModelProperty(value = "id")
	private Long id;
	@ApiModelProperty(value = "名称")
	private String name;
	@ApiModelProperty(value = "层级")
	private Integer level;
}
