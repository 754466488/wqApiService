package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class RoleIdReq extends BaseAdminReq {
	private long roleId;
}
