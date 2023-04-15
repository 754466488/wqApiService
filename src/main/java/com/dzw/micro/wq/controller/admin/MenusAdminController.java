package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.MenuTreeResp;
import com.dzw.micro.wq.resp.MenusListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.IMenuService;
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
import java.util.List;

/**
 * 菜单管理
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Api(tags = {"后台-菜单管理"})
@RestController
@RequestMapping(value = "/admin/menus")
@Slf4j
public class MenusAdminController {
	@Autowired
	private IMenuService menuService;

	@ApiOperation(value = "菜单列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<MenusListResp>> list(@Valid SelectMenuReq req, BindingResult bindingResult) {
		return menuService.findList(req);
	}

	@ApiOperation(value = "保存", notes = "")
	@PostMapping(path = "/save")
	public Resp list(@Valid SaveMenusReq req, BindingResult bindingResult) {
		return menuService.save(req);
	}

	@ApiOperation(value = "变更状态", notes = "")
	@PostMapping(path = "/updateStatus")
	public Resp updateStatus(@Valid UpdateStatusReq req, BindingResult bindingResult) {
		return menuService.updateStatus(req);
	}

	@ApiOperation(value = "获取菜单tree结构", notes = "")
	@GetMapping(path = "/treeList")
	public Resp<List<MenuTreeResp>> treeList() {
		return menuService.treeList();
	}

}
