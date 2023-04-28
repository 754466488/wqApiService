package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.StaffRoleEntity;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/27
 */
public interface StaffRoleMapper {
	StaffRoleEntity findOneById(long id);

	List<StaffRoleEntity> findAllList();

	List<StaffRoleEntity> findListByRoleId(long roleId);

	List<StaffRoleEntity> findListByStaffId(long staffId);

	int insert(StaffRoleEntity entity);

	int deleteByRoleId(long roleId);
}
