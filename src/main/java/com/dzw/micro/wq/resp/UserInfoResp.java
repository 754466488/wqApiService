package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
@Data
public class UserInfoResp {
	@ApiModelProperty(value = "用户id")
	private long staffId;
	@ApiModelProperty(value = "用户姓名")
	private String name;
	@ApiModelProperty(value = "用户名")
	private String userName;
	@ApiModelProperty(value = "用户绑定菜单")
	private List<ResourceResp> resourceResp;
}
