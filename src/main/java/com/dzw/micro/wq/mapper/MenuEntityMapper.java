package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.resp.MenusListResp;
import com.github.pagehelper.Page;

import java.util.List;


public interface MenuEntityMapper {
	MenuEntity findOneById(Long id);

	Page<MenusListResp> findList(Long pid);

	List<MenuEntity> findAll();

	int insert(MenuEntity record);

	int updateById(MenuEntity record);
}