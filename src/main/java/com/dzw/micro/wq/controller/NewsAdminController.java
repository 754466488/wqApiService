package com.dzw.micro.wq.controller;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SaveNewsReq;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.req.SetIsTopReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.NewsListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.INewsService;
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
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Api(tags = {"后台-新闻文章管理"})
@RestController
@RequestMapping(value = "/admin/news")
@Slf4j
public class NewsAdminController {
	@Autowired
	private INewsService newsAdminService;

	@ApiOperation(value = "轮播图列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<NewsListResp>> list(@Valid SelectNewsReq req, BindingResult bindingResult) {
		return newsAdminService.findList(req);
	}

	@ApiOperation(value = "保存轮播图", notes = "")
	@PostMapping(path = "/insert")
	public Resp save(@Valid SaveNewsReq req, BindingResult bindingResult) {
		return newsAdminService.save(req);
	}

	@ApiOperation(value = "变更状态", notes = "")
	@PostMapping(path = "/updateStatus")
	public Resp updateStatus(@Valid UpdateStatusReq req, BindingResult bindingResult) {
		return newsAdminService.updateStatus(req);
	}

	@ApiOperation(value = "变更设置头条状态", notes = "")
	@PostMapping(path = "/updateSetTopStatus")
	public Resp updateSetTopStatus(@Valid SetIsTopReq req, BindingResult bindingResult) {
		return newsAdminService.updateSetTopStatus(req);
	}
}
