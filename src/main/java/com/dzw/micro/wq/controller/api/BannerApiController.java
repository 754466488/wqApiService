package com.dzw.micro.wq.controller.api;


import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.resp.BannerApiListResp;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.service.IBannerService;
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
 * description
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"官网-轮播图管理"})
@RestController
@RequestMapping(value = "/api/banner")
@Slf4j
public class BannerApiController {
	@Autowired
	private IBannerService bannerService;

	@ApiOperation(value = "轮播图列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<List<BannerApiListResp>> list(@Valid BaseReq req, BindingResult bindingResult) {
		return bannerService.findPageHomeList();
	}
}
