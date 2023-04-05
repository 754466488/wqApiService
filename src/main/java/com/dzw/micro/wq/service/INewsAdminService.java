package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SaveNewsReq;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.req.SetIsTopReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.NewsListResp;
import com.dzw.micro.wq.resp.PageableDataResp;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
public interface INewsAdminService {
	Resp<PageableDataResp<NewsListResp>> findList(SelectNewsReq req);

	Resp save(SaveNewsReq req);

	Resp updateStatus(UpdateStatusReq req);
	Resp updateSetTopStatus(SetIsTopReq req);

}
