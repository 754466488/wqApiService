package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.RoleIdReq;
import com.dzw.micro.wq.req.SaveRoleReq;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.RoleAdminListResp;
import com.dzw.micro.wq.service.IRoleService;
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
 * 角色管理
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-角色管理"})
@RestController
@RequestMapping(value = "/admin/role")
@Slf4j
public class RoleAdminController {
	@Autowired
	private IRoleService roleService;

	@ApiOperation(value = "角色列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<RoleAdminListResp>> list(@Valid SelectRoleReq req, BindingResult bindingResult) {
		return roleService.list(req);
	}

	@ApiOperation(value = "保存", notes = "")
	@PostMapping(path = "/save")
	public Resp<PageableDataResp<RoleAdminListResp>> save(@Valid SaveRoleReq req, BindingResult bindingResult) {
		return roleService.save(req);
	}

	@ApiOperation(value = "删除", notes = "")
	@PostMapping(path = "/delete")
	public Resp<PageableDataResp<RoleAdminListResp>> delete(@Valid RoleIdReq req, BindingResult bindingResult) {
		return roleService.delete(req.getRoleId());
	}

}
