package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.mapper.MenuMapper;
import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.req.SaveMenusReq;
import com.dzw.micro.wq.req.SelectMenuReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.LeftMenuTreeResp;
import com.dzw.micro.wq.resp.MenuTreeResp;
import com.dzw.micro.wq.resp.MenusListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.IMenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
@Service
public class MenuServiceImpl implements IMenuService {
	@Autowired
	private MenuMapper menuMapper;

	@Override
	public Resp<PageableDataResp<MenusListResp>> findList(SelectMenuReq req) {
		PageableDataResp<MenusListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<MenusListResp> respPage = menuMapper.findList(req.getPid());
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp save(SaveMenusReq req) {
		Long id = req.getId();
		if (Objects.isNull(id)) {
			MenuEntity menuEntity = new MenuEntity();
			BeanUtils.copyProperties(menuEntity, req);
			menuEntity.setReq(0);
			menuEntity.setPicUrl("");
			menuEntity.setStatus(EnableStatusEnum.ENABLE.getCode());
			menuEntity.setCreateTime(DateUtils.currentTimeSecond());
			menuEntity.setCreateUser(req.getUserName());
			menuMapper.insert(menuEntity);
		} else {
			MenuEntity menuEntity = menuMapper.findOneById(id);
			if (Objects.isNull(menuEntity)) {
				return Resp.error("数据不存在");
			}
			menuEntity.setPicUrl(req.getPicUrl());
			menuEntity.setName(req.getName());
			menuEntity.setUpdateTime(DateUtils.currentTimeSecond());
			menuEntity.setUpdateUser(req.getUserName());
			menuMapper.updateById(menuEntity);
		}
		return Resp.success();
	}

	@Override
	public Resp updateStatus(UpdateStatusReq req) {
		MenuEntity menuEntity = menuMapper.findOneById(req.getId());
		if (Objects.isNull(menuEntity)) {
			return Resp.error("数据不存在");
		}
		menuEntity.setStatus(req.getStatus());
		menuEntity.setUpdateTime(DateUtils.currentTimeSecond());
		menuEntity.setUpdateUser(req.getUserName());
		menuMapper.updateById(menuEntity);
		return Resp.success();
	}

	@Override
	public Resp<List<MenuTreeResp>> treeList(Long staffId) {
		List<Long> menuIds = Lists.newArrayList();
		if (Objects.nonNull(staffId)) {
			List<MenuEntity> list = menuMapper.findListByStaffId(staffId);
			if (CollectionUtils.isNotEmpty(list)) {
				menuIds = list.stream().map(MenuEntity::getId).collect(Collectors.toList());
			}
		}
		List<Long> menuIds2 = menuIds;
		List<MenuEntity> list = menuMapper.findAll();
		List<MenuEntity> oneList = list.stream().filter(x -> menuIds2.contains(x.getPid())).collect(Collectors.toList());
		List<MenuTreeResp> respList = Lists.newArrayList();
		for (MenuEntity entity : oneList
		) {
			MenuTreeResp resp = new MenuTreeResp();
			BeanUtils.copyProperties(resp, entity);
			List<MenuEntity> twoList = list.stream().filter(x -> x.getPid().equals(entity.getId())).collect(Collectors.toList());
			List<MenuTreeResp> twoRespList = Lists.newArrayList();
			for (MenuEntity two : twoList
			) {
				MenuTreeResp twoResp = new MenuTreeResp();
				BeanUtils.copyProperties(twoResp, two);
				List<MenuEntity> threeList = list.stream().filter(x -> x.getPid().equals(two.getId())).collect(Collectors.toList());
				List<MenuTreeResp> threeRespList = Lists.newArrayList();
				for (MenuEntity three : threeList
				) {
					MenuTreeResp threeResp = new MenuTreeResp();
					BeanUtils.copyProperties(threeResp, three);
					threeResp.setChildMenuList(new ArrayList<>());
					threeRespList.add(threeResp);
				}
				twoResp.setChildMenuList(threeRespList);
				twoRespList.add(twoResp);
			}
			resp.setChildMenuList(twoRespList);
			respList.add(resp);
		}
		return Resp.success(respList);
	}

	@Override
	public Resp<List<LeftMenuTreeResp>> leftTreeList(Long menuId) {
		MenuEntity menuEntity = menuMapper.findOneById(menuId);
		if (Objects.isNull(menuEntity)) {
			return Resp.success();
		}
		List<LeftMenuTreeResp> respList = Lists.newArrayList();
		List<MenuEntity> list = menuMapper.findAll();
		List<MenuEntity> entityList = Lists.newArrayList();
		int level = menuEntity.getLevel();
		if (level == 1) {
			entityList = list.stream().filter(x -> x.getPid().equals(menuEntity.getId())).collect(Collectors.toList());
		} else if (level == 2) {
			//一级
			List<MenuEntity> entityList1 = list.stream().filter(x -> x.getId().equals(menuEntity.getPid())).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(entityList1)) {
				entityList.add(entityList1.get(0));
			}
			//二级
			List<MenuEntity> entityList2 = list.stream().filter(x -> x.getPid().equals(menuEntity.getPid())).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(entityList2)) {
				entityList.addAll(entityList2);
			}
			//三级
			List<MenuEntity> entityList3 = list.stream().filter(x -> x.getPid().equals(menuEntity.getId())).collect(Collectors.toList());
			for (int i = 0; i < entityList2.size(); i++) {
				if (entityList2.get(i).getId().equals(menuEntity.getId())) {
					entityList.addAll(i + 2, entityList3);
				}
			}
		} else if (level == 3) {
			List<MenuEntity> entityList1 = list.stream().filter(x -> x.getId().equals(menuEntity.getPid())).collect(Collectors.toList());
			if (CollectionUtils.isNotEmpty(entityList1)) {
				long ppid = entityList1.get(0).getPid();
				//一级
				List<MenuEntity> entityList2 = list.stream().filter(x -> x.getId().equals(ppid)).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(entityList2)) {
					entityList.add(entityList2.get(0));
				}
				//二级
				List<MenuEntity> entityList3 = list.stream().filter(x -> x.getPid().equals(ppid)).collect(Collectors.toList());
				if (CollectionUtils.isNotEmpty(entityList3)) {
					entityList.addAll(entityList3);
				}
				//三级
				List<MenuEntity> entityList4 = list.stream().filter(x -> x.getPid().equals(menuEntity.getPid())).collect(Collectors.toList());
				for (int i = 0; i < entityList3.size(); i++) {
					if (entityList3.get(i).getId().equals(menuEntity.getPid())) {
						entityList.addAll(i + 2, entityList4);
					}
				}
			}
		}
		for (MenuEntity entity : entityList
		) {
			LeftMenuTreeResp resp = new LeftMenuTreeResp();
			resp.setId(entity.getId());
			resp.setPid(entity.getPid());
			resp.setLevel(entity.getLevel());
			resp.setName(entity.getName());
			resp.setPicUrl(entity.getPicUrl());
			respList.add(resp);
		}

		return Resp.success(respList);
	}
}
