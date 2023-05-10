package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/10
 */
@Data
public class UserIdReq extends BaseAdminReq {
	private Long userId;
}
