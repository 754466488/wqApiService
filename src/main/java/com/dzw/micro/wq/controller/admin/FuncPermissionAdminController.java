package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.RoleIdReq;
import com.dzw.micro.wq.service.IFuncPermissionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * 角色菜单关系管理
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-角色菜单关系管理"})
@RestController
@RequestMapping(value = "/admin/roleResource")
@Slf4j
public class FuncPermissionAdminController {
	@Autowired
	private IFuncPermissionService funcPermissionServicel;

	@ApiOperation(value = "获取角色绑定菜单列表", notes = "")
	@GetMapping(path = "/bindList")
	public Resp bindList(@Valid RoleIdReq req, BindingResult bindingResult) {
		return funcPermissionServicel.bindList(req.getRoleId());
	}
}
