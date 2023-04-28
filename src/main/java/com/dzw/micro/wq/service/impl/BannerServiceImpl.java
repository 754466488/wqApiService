package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.mapper.BannerMapper;
import com.dzw.micro.wq.model.BannerEntity;
import com.dzw.micro.wq.req.SaveBannerReq;
import com.dzw.micro.wq.req.SelectBannerReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.BannerApiListResp;
import com.dzw.micro.wq.resp.BannerListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.IBannerService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * 轮播图管理服务
 *
 * @author lyb
 * @date created in 2023/4/4
 */
@Service
public class BannerServiceImpl implements IBannerService {
	@Autowired
	private BannerMapper bannerMapper;

	/**
	 * 获取首页轮播图列表
	 *
	 * @author: lyb
	 * @date: 2023/4/9 00:36
	 */
	@Override
	public Resp<List<BannerApiListResp>> findPageHomeList() {
		List<BannerApiListResp> pageHomeList = bannerMapper.findPageHomeList();
		return Resp.success(pageHomeList);
	}

	/**
	 * 获取轮播图列表
	 *
	 * @author: lyb
	 * @date: 2023/4/4 22:36
	 */
	@Override
	public Resp<PageableDataResp<BannerListResp>> findPageList(SelectBannerReq req) {
		PageableDataResp<BannerListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<BannerListResp> respPage = bannerMapper.findPageList(req.getStatus(), req.getType());
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	/**
	 * 添加轮播图
	 *
	 * @author: lyb
	 * @date: 2023/4/5 21:04
	 */
	@Override
	public Resp save(SaveBannerReq req) {
		Long id = req.getId();
		if (Objects.isNull(id)) {
			BannerEntity bannerEntity = new BannerEntity();
			BeanUtils.copyProperties(bannerEntity, req);
			bannerEntity.setReq(0);
			bannerEntity.setStatus(EnableStatusEnum.DISABLE.getCode());
			bannerEntity.setCreateTime(DateUtils.currentTimeSecond());
			bannerEntity.setCreateUser(req.getUserName());
			bannerMapper.insert(bannerEntity);
		} else {
			BannerEntity bannerEntity = bannerMapper.findOneById(id);
			if (Objects.isNull(bannerEntity)) {
				return Resp.error("数据不存在");
			}
			bannerEntity.setTitle(req.getTitle());
			bannerEntity.setType(req.getType());
			bannerEntity.setUrl(req.getUrl());
			bannerEntity.setLinkUrl(req.getLinkUrl());
			bannerEntity.setUpdateTime(DateUtils.currentTimeSecond());
			bannerEntity.setUpdateUser(req.getUserName());
			bannerMapper.updateById(bannerEntity);
		}
		return Resp.success();
	}

	/**
	 * 变更状态
	 *
	 * @author: lyb
	 * @date: 2023/4/5 21:21
	 */
	@Override
	public Resp updateStatus(UpdateStatusReq req) {
		BannerEntity bannerEntity = bannerMapper.findOneById(req.getId());
		if (Objects.isNull(bannerEntity)) {
			return Resp.error("数据不存在");
		}
		bannerEntity.setStatus(req.getStatus());
		bannerEntity.setUpdateTime(DateUtils.currentTimeSecond());
		bannerEntity.setUpdateUser(req.getUserName());
		bannerMapper.updateById(bannerEntity);
		return Resp.success();
	}

}
