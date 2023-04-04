package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.resp.PageableDataResp;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/4
 */
public interface IBannerAdminService {
	Resp<PageableDataResp<BannerListResp>> findList(SelectBannerReq req);
}
