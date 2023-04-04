package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.mapper.BannerEntityMapper;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.IBannerAdminService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 轮播图管理服务
 *
 * @author lyb
 * @date created in 2023/4/4
 */
@Service
public class BannerAdminServiceImpl implements IBannerAdminService {
	@Autowired
	private BannerEntityMapper bannerEntityMapper;


	/**
	 * 获取轮播图列表
	 *
	 * @author: lyb
	 * @date: 2023/4/4 22:36
	 */
	@Override
	public Resp<PageableDataResp<BannerListResp>> findList(SelectBannerReq req) {
		PageableDataResp<BannerListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<BannerListResp> respPage = bannerEntityMapper.findList(req.getStatus(), req.getType());
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}
}
