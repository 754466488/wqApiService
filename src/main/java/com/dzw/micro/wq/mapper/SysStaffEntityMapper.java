package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.SysStaffEntity;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
public interface SysStaffEntityMapper {
	SysStaffEntity findOneByUserName(String userName);
}