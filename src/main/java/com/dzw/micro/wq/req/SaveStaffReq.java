package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class SaveStaffReq extends BaseAdminReq {
	@ApiModelProperty(value = "id")
	private Long staffId;
	@NotBlank(message = "用户名称不能为空")
	@ApiModelProperty(value = "用户名称")
	private String name;
	@NotBlank(message = "密码不能为空")
	@ApiModelProperty(value = "密码")
	private String pass;
	@ApiModelProperty(value = "手机号码")
	private String phone;
	@ApiModelProperty(value = "身份证号码")
	private String idCard;
	@ApiModelProperty(value = "职位")
	private String positions;
	@ApiModelProperty(value = "状态 0启用1停用")
	private int status;
	@NotBlank(message = "操作人不能为空")
	@ApiModelProperty(value = "操作人")
	private String createUser;
}
