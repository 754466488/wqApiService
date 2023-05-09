package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.BindRoleReq;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.req.StaffIdReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.RoleAdminListResp;
import com.dzw.micro.wq.service.IStaffRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 用户角色关系管理
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-用户角色关系管理"})
@RestController
@RequestMapping(value = "/admin/staffRole")
@Slf4j
public class StaffRoleAdminController {
	@Autowired
	IStaffRoleService staffRoleService;

	@ApiOperation(value = "获取用户已绑定角色列表", notes = "")
	@GetMapping(path = "/bindList")
	public Resp bindList(@Valid BaseAdminReq req, BindingResult bindingResult) {
		return staffRoleService.getBindList(req.getStaffId());
	}

	@ApiOperation(value = "绑定角色", notes = "")
	@PostMapping(path = "/bind")
	public Resp bind(@Valid BindRoleReq req, BindingResult bindingResult) {
		return staffRoleService.bind(req);
	}
}
