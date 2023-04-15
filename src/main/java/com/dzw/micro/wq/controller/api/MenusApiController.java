package com.dzw.micro.wq.controller.api;

import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.resp.MenuTreeResp;
import com.dzw.micro.wq.service.IMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
@Api(tags = {"官网-菜单管理"})
@RestController
@RequestMapping(value = "/api/menus")
@Slf4j
public class MenusApiController {
	@Autowired
	private IMenuService menuService;

	@ApiOperation(value = "获取菜单tree结构", notes = "")
	@GetMapping(path = "/treeList")
	public Resp<List<MenuTreeResp>> treeList(@Valid BaseReq req, BindingResult bindingResult) {
		return menuService.treeList();
	}

}
