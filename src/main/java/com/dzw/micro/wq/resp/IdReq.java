package com.dzw.micro.wq.resp;

import com.dzw.micro.wq.application.domain.req.BaseReq;
import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/8
 */
@Data
public class IdReq extends BaseReq {
	private Long id;
}
