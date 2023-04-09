package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.enums.EnableStatusEnum;
import com.dzw.micro.wq.mapper.MenuEntityMapper;
import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.req.SaveMenusReq;
import com.dzw.micro.wq.req.SelectMenuReq;
import com.dzw.micro.wq.req.UpdateStatusReq;
import com.dzw.micro.wq.resp.MenuTreeResp;
import com.dzw.micro.wq.resp.MenusListResp;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.service.IMenuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
	private MenuEntityMapper menuEntityMapper;

	@Override
	public Resp<PageableDataResp<MenusListResp>> findList(SelectMenuReq req) {
		PageableDataResp<MenusListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<MenusListResp> respPage = menuEntityMapper.findList(req.getPid());
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
			menuEntity.setStatus(EnableStatusEnum.ENABLE.getCode());
			menuEntity.setCreateTime(DateUtils.currentTimeSecond());
			menuEntity.setCreateUser(req.getUserName());
			menuEntityMapper.insert(menuEntity);
		} else {
			MenuEntity menuEntity = menuEntityMapper.findOneById(id);
			if (Objects.isNull(menuEntity)) {
				return Resp.error("数据不存在");
			}
			menuEntity.setName(req.getName());
			menuEntity.setUpdateTime(DateUtils.currentTimeSecond());
			menuEntity.setUpdateUser(req.getUserName());
			menuEntityMapper.updateById(menuEntity);
		}
		return Resp.success();
	}

	@Override
	public Resp updateStatus(UpdateStatusReq req) {
		MenuEntity menuEntity = menuEntityMapper.findOneById(req.getId());
		if (Objects.isNull(menuEntity)) {
			return Resp.error("数据不存在");
		}
		menuEntity.setStatus(req.getStatus());
		menuEntity.setUpdateTime(DateUtils.currentTimeSecond());
		menuEntity.setUpdateUser(req.getUserName());
		menuEntityMapper.updateById(menuEntity);
		return Resp.success();
	}

	@Override
	public Resp<List<MenuTreeResp>> treeList() {
		List<MenuEntity> list = menuEntityMapper.findAll();
		List<MenuEntity> oneList = list.stream().filter(x -> x.getPid() == 0).collect(Collectors.toList());
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
}
