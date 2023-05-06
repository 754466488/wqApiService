package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.service.IResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 菜单资源管理
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-菜单资源管理"})
@RestController
@RequestMapping(value = "/admin/resource")
@Slf4j
public class ResourceAdminController {
	@Autowired
	private IResourceService resourceService;

	@ApiOperation(value = "获取角色绑定菜单列表", notes = "")
	@GetMapping(path = "/list")
	public Resp list() {
		return resourceService.list();
	}
}
