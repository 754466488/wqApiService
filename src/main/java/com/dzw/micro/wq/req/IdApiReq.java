package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseApiReq;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/8
 */
@Data
public class IdApiReq extends BaseApiReq {
	private Long id;
}
