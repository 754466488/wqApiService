package com.dzw.micro.wq.controller.api;

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
@Api(tags = {"官网-新闻文章管理"})
@RestController
@RequestMapping(value = "/api/news")
@Slf4j
public class NewsApiController {
	@Autowired
	private INewsService newsAdminService;

	@ApiOperation(value = "新闻文章列表", notes = "")
	@GetMapping(path = "/list")
	public Resp<PageableDataResp<NewsListResp>> list(@Valid SelectNewsReq req, BindingResult bindingResult) {
		return newsAdminService.findList(req);
	}
}
