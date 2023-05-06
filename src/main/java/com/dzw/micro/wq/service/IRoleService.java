package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SaveRoleReq;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.RoleAdminListResp;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
public interface IRoleService {
	Resp<PageableDataResp<RoleAdminListResp>> list(SelectRoleReq req);

	Resp save(SaveRoleReq req);

	Resp delete(long roleId);
}
