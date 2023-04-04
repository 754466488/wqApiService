package com.dzw.micro.wq.controller;


import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.service.IBannerAdminService;
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
 * description
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-轮播图管理"})
@RestController
@RequestMapping(value = "/admin/banner")
@Slf4j
public class BannerApiController {

	@Autowired
	private IBannerAdminService bannerAdminService;

	@ApiOperation(value = "轮播图列表", notes = "")
	@GetMapping(path = "/list")
	public Resp list(@Valid SelectBannerReq req, BindingResult bindingResult) {
		return bannerAdminService.findList(req);
	}
}
