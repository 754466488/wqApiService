package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/10
 */
@Data
public class UserIdReq extends BaseAdminReq {
	@NotNull(message = "用户id不能为空")
	@ApiModelProperty(value = "用户id")
	private Long userId;
}
