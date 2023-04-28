package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.mapper.MenuMapper;
import com.dzw.micro.wq.mapper.NewsMapper;
import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.model.NewsEntity;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.NewsApiListResp;
import com.dzw.micro.wq.resp.NewsDetailResp;
import com.dzw.micro.wq.resp.NewsListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.INewsService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Service
public class NewsServiceImpl implements INewsService {
	@Autowired
	private NewsMapper newsMapper;
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Resp<List<NewsApiListResp>> findPageHomeNewsList() {
		List<NewsApiListResp> newsList = newsMapper.findPageHomeNewsList();
		return Resp.success(newsList);
	}

	@Override
	public Resp<PageableDataResp<NewsListResp>> findPageList(SelectNewsReq req) {
		PageableDataResp<NewsListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<NewsListResp> respPage = newsMapper.findList(req);
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp<PageableDataResp<NewsApiListResp>> findPageContentList(SelectNewsReq req) {
		PageableDataResp<NewsApiListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<NewsApiListResp> respPage = newsMapper.findContentList(req);
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp<List<NewsListResp>> findList(SelectNewsReq req) {
		return Resp.success();
	}

	@Override
	public Resp save(SaveNewsReq req) {
		Long id = req.getId();
		if (Objects.isNull(id)) {
			MenuEntity menuEntity = menuMapper.findOneById(req.getMenuId());
			NewsEntity entity = new NewsEntity();
			BeanUtils.copyProperties(entity, req);
			entity.setClickNum(1);
			entity.setIsTop(0);
			entity.setType(0);
			entity.setLinkUrl("");
			entity.setMenuName("");
			if (Objects.nonNull(menuEntity)) {
				entity.setMenuName(menuEntity.getName());
			}
			entity.setStatus(EnableStatusEnum.WAIT_PUBLISH.getCode());
			entity.setCreateTime(DateUtils.currentTimeSecond());
			entity.setCreateUser(req.getUserName());
			newsMapper.insert(entity);
		} else {
			NewsEntity entity = newsMapper.findOneById(id);
			if (Objects.isNull(entity)) {
				return Resp.error("数据不存在");
			}
			MenuEntity menuEntity = menuMapper.findOneById(req.getMenuId());
			BeanUtils.copyProperties(entity, req);
			if (Objects.nonNull(menuEntity)) {
				entity.setMenuName(menuEntity.getName());
			}
			entity.setUpdateTime(DateUtils.currentTimeSecond());
			entity.setUpdateUser(req.getUserName());
			newsMapper.updateById(entity);
		}
		return Resp.success();
	}

	/**
	 * 变更状态
	 *
	 * @author: lyb
	 * @date: 2023/4/5 23:39
	 */
	@Override
	public Resp updateStatus(UpdateStatusReq req) {
		NewsEntity entity = newsMapper.findOneById(req.getId());
		if (Objects.isNull(entity)) {
			return Resp.error("数据不存在");
		}
		if (req.getStatus() == 2) {
			entity.setPublishTime(DateUtils.currentTimeSecond());
			req.setStatus(0);
		}
		entity.setStatus(req.getStatus());
		entity.setUpdateTime(DateUtils.currentTimeSecond());
		entity.setUpdateUser(req.getUserName());
		newsMapper.updateById(entity);
		return Resp.success();
	}

	@Override
	public Resp updateSetTopStatus(SetIsTopReq req) {
		if (req.getIsTop() == 1 && newsMapper.existsTop(req.getIsTop())) {
			return Resp.error("当前已存在头条文章，请先停用！");
		}
		NewsEntity entity = newsMapper.findOneById(req.getId());
		if (Objects.isNull(entity)) {
			return Resp.error("数据不存在");
		}
		entity.setIsTop(req.getIsTop());
		entity.setUpdateTime(DateUtils.currentTimeSecond());
		entity.setUpdateUser(req.getUserName());
		newsMapper.updateById(entity);
		return Resp.success();
	}

	@Override
	public Resp<NewsDetailResp> detail(long id, boolean addClick) {
		NewsEntity newsEntity = newsMapper.findOneById(id);
		if (Objects.isNull(newsEntity)) {
			return Resp.success();
		}
		if (addClick) {
			//记录点击次数
			newsMapper.addClickNumById(id);
		}
		//上一篇
		NewsEntity upOne = newsMapper.findUpOne(id, newsEntity.getMenuId());
		//上一篇
		NewsEntity downOne = newsMapper.findDownOne(id, newsEntity.getMenuId());
		NewsDetailResp resp = new NewsDetailResp();
		BeanUtils.copyProperties(resp, newsEntity);
		if (Objects.nonNull(upOne)) {
			resp.setUpNewsId(upOne.getId());
			resp.setUpNewsTitle(upOne.getTitle());
		}
		if (Objects.nonNull(downOne)) {
			resp.setDownNewsId(downOne.getId());
			resp.setDownNewsTitle(downOne.getTitle());
		}
		return Resp.success(resp);
	}
}
