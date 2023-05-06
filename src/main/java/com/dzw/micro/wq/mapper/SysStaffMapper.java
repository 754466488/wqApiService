package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.SysStaffEntity;
import org.mapstruct.Mapper;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
@Mapper
public interface SysStaffMapper {
	SysStaffEntity findOneByUserName(String userName);
}