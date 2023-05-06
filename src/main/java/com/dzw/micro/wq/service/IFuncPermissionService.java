package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.BindResourceReq;
import com.dzw.micro.wq.resp.RoleAdminListResp;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
public interface IFuncPermissionService {
	Resp<List<RoleAdminListResp>> bindList(long roleId);

	Resp bind(BindResourceReq req);
}
