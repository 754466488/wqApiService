package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.BindRoleReq;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
public interface IStaffRoleService {
	Resp<List<Long>> getBindList(long staffId);

	Resp bind(BindRoleReq req);
}
