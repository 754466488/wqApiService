package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Data
public class SaveMenusReq {
	@ApiModelProperty(value = "id")
	private Long id;
	@NotNull(message = "父级id不能为空")
	@ApiModelProperty(value = "父级id")
	private Long pid;
	@NotNull(message = "层级不能为空")
	@ApiModelProperty(value = "层级")
	private Integer level;
	@NotBlank(message = "名称不能为空")
	@ApiModelProperty(value = "名称")
	private String name;
	@NotBlank(message = "用户名不能为空")
	@ApiModelProperty(value = "用户名")
	private String userName;
	@ApiModelProperty(value = "图片地址")
	private String picUrl;
}
