package com.dzw.micro.wq.mapper;


import com.dzw.micro.wq.model.FuncPermissionEntity;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/27
 */
@Mapper
public interface FuncPermissionMapper {

	FuncPermissionEntity findOneById(long id);

	List<FuncPermissionEntity> findAllList();

	List<FuncPermissionEntity> findListByRoleIds(List<Long> roleIds);

	List<FuncPermissionEntity> findListByRoleId(long roleId);

	int insert(FuncPermissionEntity entity);

	int deleteByRoleId(long roleId);
}
