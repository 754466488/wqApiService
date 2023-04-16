package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.mapper.MenuEntityMapper;
import com.dzw.micro.wq.mapper.NewsEntityMapper;
import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.model.NewsEntity;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.NewsApiListResp;
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
	private NewsEntityMapper newsEntityMapper;
	@Autowired
	private MenuEntityMapper menuEntityMapper;

	@Override
	public Resp<List<NewsApiListResp>> findPageHomeNewsList() {
		List<NewsApiListResp> newsList = newsEntityMapper.findPageHomeNewsList();
		return Resp.success(newsList);
	}

	@Override
	public Resp<PageableDataResp<NewsListResp>> findPageList(SelectNewsReq req) {
		PageableDataResp<NewsListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<NewsListResp> respPage = newsEntityMapper.findList(req);
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp<PageableDataResp<NewsListResp>> findPageContentList(SelectNewsReq req) {
		PageableDataResp<NewsListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<NewsListResp> respPage = newsEntityMapper.findContentList(req);
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
			MenuEntity menuEntity = menuEntityMapper.findOneById(req.getMenuId());
			NewsEntity entity = new NewsEntity();
			BeanUtils.copyProperties(entity, req);
			entity.setClickNum(0);
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
			newsEntityMapper.insert(entity);
		} else {
			NewsEntity entity = newsEntityMapper.findOneById(id);
			if (Objects.isNull(entity)) {
				return Resp.error("数据不存在");
			}
			BeanUtils.copyProperties(entity, req);
			entity.setUpdateTime(DateUtils.currentTimeSecond());
			entity.setUpdateUser(req.getUserName());
			newsEntityMapper.updateById(entity);
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
		NewsEntity entity = newsEntityMapper.findOneById(req.getId());
		if (Objects.isNull(entity)) {
			return Resp.error("数据不存在");
		}
		if (req.getStatus() == 2) {
			entity.setPublishTime(DateUtils.currentTimeSecond());
		}
		entity.setStatus(req.getStatus());
		entity.setUpdateTime(DateUtils.currentTimeSecond());
		entity.setUpdateUser(req.getUserName());
		newsEntityMapper.updateById(entity);
		return Resp.success();
	}

	@Override
	public Resp updateSetTopStatus(SetIsTopReq req) {
		if (req.getIsTop() == 1 && newsEntityMapper.existsTop(req.getIsTop())) {
			return Resp.error("当前已存在头条文章，请先停用！");
		}
		NewsEntity entity = newsEntityMapper.findOneById(req.getId());
		if (Objects.isNull(entity)) {
			return Resp.error("数据不存在");
		}
		entity.setIsTop(req.getIsTop());
		entity.setUpdateTime(DateUtils.currentTimeSecond());
		entity.setUpdateUser(req.getUserName());
		newsEntityMapper.updateById(entity);
		return Resp.success();
	}

	@Override
	public Resp<NewsListResp> detail(long id, boolean addClick) {
		NewsEntity newsEntity = newsEntityMapper.findOneById(id);
		if (Objects.isNull(newsEntity)) {
			return Resp.success();
		}
		if (addClick) {
			//记录点击次数
			newsEntityMapper.addClickNumById(id);
		}
		return Resp.success(newsEntity);
	}
}
