package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 分页
 *
 * @author lyb
 * @date created in 2023/4/4
 */
@Data
public class AdminPageReq extends BaseAdminReq {
	@ApiModelProperty(value = "页数,默认1")
	private int pageNo = 1;
	@ApiModelProperty(value = "每页大小,默认10")
	private int pageSize = 10;
}
