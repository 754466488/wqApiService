package com.dzw.micro.wq.controller.admin;


import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.enums.BannerPositionEnum;
import com.dzw.micro.wq.req.SaveBannerReq;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.SelectedResp;
import com.dzw.micro.wq.service.IBannerService;
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
 * description
 *
 * @author: lyb
 * @date: 2023/4/4 00:07
 */
@Api(tags = {"后台-轮播图管理"})
@RestController
@RequestMapping(value = "/admin/banner")
@Slf4j
public class BannerAdminController {

	@Autowired
	private IBannerService bannerAdminService;

	@ApiOperation(value = "轮播图列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<BannerListResp>> list(@Valid SelectBannerReq req, BindingResult bindingResult) {
		return bannerAdminService.findPageList(req);
	}

	@ApiOperation(value = "保存轮播图", notes = "")
	@PostMapping(path = "/insert")
	public Resp save(@Valid SaveBannerReq req, BindingResult bindingResult) {
		return bannerAdminService.save(req);
	}

	@ApiOperation(value = "变更状态", notes = "")
	@PostMapping(path = "/updateStatus")
	public Resp updateStatus(@Valid UpdateStatusReq req, BindingResult bindingResult) {
		return bannerAdminService.updateStatus(req);
	}
}
