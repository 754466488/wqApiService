package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.NewsApiListResp;
import com.dzw.micro.wq.resp.NewsListResp;
import com.dzw.micro.wq.resp.PageableDataResp;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
public interface INewsService {
	Resp<List<NewsApiListResp>> findPageHomeNewsList();

	Resp<PageableDataResp<NewsListResp>> findPageList(SelectNewsReq req);

	Resp<List<NewsListResp>> findList(SelectNewsReq req);

	Resp save(SaveNewsReq req);

	Resp updateStatus(UpdateStatusReq req);

	Resp updateSetTopStatus(SetIsTopReq req);

	Resp<NewsListResp> detail(long id);
}
