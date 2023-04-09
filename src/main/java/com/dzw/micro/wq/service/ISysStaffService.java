package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.LoginReq;
import com.dzw.micro.wq.resp.UserInfoResp;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
public interface ISysStaffService {
	Resp<UserInfoResp> login(LoginReq req);
}
