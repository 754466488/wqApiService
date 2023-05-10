package com.dzw.micro.wq.controller.admin;


import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.SysStaffListResp;
import com.dzw.micro.wq.resp.UserInfoResp;
import com.dzw.micro.wq.service.ISysStaffService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户管理
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-用户管理"})
@RestController
@RequestMapping(value = "/admin/sysStaff")
@Slf4j
public class SysStaffAdminController {

	@Autowired
	private ISysStaffService sysStaffService;

	@ApiOperation(value = "用户列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<SysStaffListResp>> list(@Valid SelectStaffReq req, BindingResult bindingResult) {
		return sysStaffService.list(req);
	}

	@ApiOperation(value = "登录", notes = "")
	@GetMapping(path = "/login")
	public Resp<UserInfoResp> list(@Valid LoginReq req, BindingResult bindingResult) {
		return sysStaffService.login(req);
	}

	@ApiOperation(value = "保存用户", notes = "")
	@PostMapping(path = "/save")
	public Resp save(@Valid SaveStaffReq req, BindingResult bindingResult) {
		return sysStaffService.save(req);
	}

	@ApiOperation(value = "删除用户", notes = "")
	@PostMapping(path = "/delete")
	public Resp delete(@Valid UserIdReq req, BindingResult bindingResult) {
		return sysStaffService.delete(req.getUserId());
	}
}
