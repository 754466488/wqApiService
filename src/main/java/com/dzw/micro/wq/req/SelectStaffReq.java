package com.dzw.micro.wq.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class SelectStaffReq extends PageReq {
	@ApiModelProperty(value = "员工姓名")
	private String name;
	@ApiModelProperty(value = "手机号")
	private String phone;
	@ApiModelProperty(value = "身份证号码")
	private String idCard;
	@ApiModelProperty(value = "员工职位")
	private String positions;
}
