package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.mapper.SysStaffEntityMapper;
import com.dzw.micro.wq.model.SysStaffEntity;
import com.dzw.micro.wq.req.LoginReq;
import com.dzw.micro.wq.resp.UserInfoResp;
import com.dzw.micro.wq.service.ISysStaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
@Service
public class SysStaffServiceImpl implements ISysStaffService {
	@Autowired
	private SysStaffEntityMapper sysStaffEntityMapper;

	@Override
	public Resp<UserInfoResp> login(LoginReq req) {
		SysStaffEntity sysStaffEntity = sysStaffEntityMapper.findOneByUserName(req.getUserName());
		if (Objects.isNull(sysStaffEntity)) {
			return Resp.error("该用户不存在");
		}
		if (!Objects.equals(req.getPassword(), sysStaffEntity.getPass())) {
			return Resp.error("密码错误");
		}

		UserInfoResp resp = new UserInfoResp();
		resp.setStaffId(sysStaffEntity.getStaffId());
		resp.setName(sysStaffEntity.getName());
		resp.setUserName(sysStaffEntity.getUserName());
		return Resp.success(resp);
	}
}
