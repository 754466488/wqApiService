package com.dzw.micro.wq.controller.api;

import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.req.SelectNewsApiReq;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.resp.IdReq;
import com.dzw.micro.wq.resp.NewsApiListResp;
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
import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Api(tags = {"官网-新闻文章管理"})
@RestController
@RequestMapping(value = "/api/news")
@Slf4j
public class NewsApiController {
	@Autowired
	private INewsService newsService;

	@ApiOperation(value = "首页新闻文章列表", notes = "")
	@GetMapping(path = "/pageHomeList")
	public Resp<List<NewsApiListResp>> findPageHomeNewsList(@Valid BaseReq req, BindingResult bindingResult) {
		return newsService.findPageHomeNewsList();
	}

	@ApiOperation(value = "新闻文章列表-无文章内容", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<NewsListResp>> list(@Valid SelectNewsApiReq req, BindingResult bindingResult) {
		SelectNewsReq newsReq = new SelectNewsReq();
		newsReq.setStatus(EnableStatusEnum.ENABLE.getCode());
		newsReq.setMenuId(req.getMenuId());
		newsReq.setPageNo(req.getPageNo());
		newsReq.setPageSize(req.getPageSize());
		newsReq.setIsGw(0);
		return newsService.findPageList(newsReq);
	}

	@ApiOperation(value = "新闻文章列表-有文章内容", notes = "")
	@GetMapping(path = "/listContent")
	public Resp<PageableDataResp<NewsApiListResp>> listContent(@Valid SelectNewsApiReq req, BindingResult bindingResult) {
		SelectNewsReq newsReq = new SelectNewsReq();
		newsReq.setStatus(EnableStatusEnum.ENABLE.getCode());
		newsReq.setMenuId(req.getMenuId());
		newsReq.setPageNo(req.getPageNo());
		newsReq.setPageSize(req.getPageSize());
		return newsService.findPageContentList(newsReq);
	}

	@ApiOperation(value = "获取文章详情", notes = "")
	@PostMapping(path = "/detail")
	public Resp detail(@Valid IdReq req, BindingResult bindingResult) {
		return newsService.detail(req.getId(), true);
	}
}
