package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.MenuEntity;

public interface MenuEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MenuEntity record);

    int insertSelective(MenuEntity record);

    MenuEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MenuEntity record);

    int updateByPrimaryKey(MenuEntity record);
}