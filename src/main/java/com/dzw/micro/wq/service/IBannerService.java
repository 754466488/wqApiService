package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.SaveBannerReq;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.BannerApiListResp;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.resp.PageableDataResp;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/4
 */
public interface IBannerService {
	Resp<List<BannerApiListResp>> findPageHomeList();

	Resp<PageableDataResp<BannerListResp>> findPageList(SelectBannerReq req);

	Resp save(SaveBannerReq req);

	Resp updateStatus(UpdateStatusReq req);

}
