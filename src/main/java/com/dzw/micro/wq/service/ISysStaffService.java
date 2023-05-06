package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.LoginReq;
import com.dzw.micro.wq.req.SaveStaffReq;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.req.SelectStaffReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.SysStaffListResp;
import com.dzw.micro.wq.resp.UserInfoResp;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
public interface ISysStaffService {
	Resp<PageableDataResp<SysStaffListResp>> list(SelectStaffReq req);

	Resp<UserInfoResp> login(LoginReq req);

	Resp save(SaveStaffReq req);

	Resp delete(long staffId);
}
