package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.SysStaffEntity;

public interface SysStaffEntityMapper {
    int deleteByPrimaryKey(String staffId);

    int insert(SysStaffEntity record);

    int insertSelective(SysStaffEntity record);

    SysStaffEntity selectByPrimaryKey(String staffId);

    int updateByPrimaryKeySelective(SysStaffEntity record);

    int updateByPrimaryKey(SysStaffEntity record);
}