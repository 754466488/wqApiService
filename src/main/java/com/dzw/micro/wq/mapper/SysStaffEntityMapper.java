package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.SysStaffEntity;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
public interface SysStaffEntityMapper {
	int deleteByPrimaryKey(String staffId);

	int insert(SysStaffEntity record);

	int insertSelective(SysStaffEntity record);

	SysStaffEntity selectByPrimaryKey(String staffId);

	int updateByPrimaryKeySelective(SysStaffEntity record);

	int updateByPrimaryKey(SysStaffEntity record);
}