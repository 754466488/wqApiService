package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.RoleEntity;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.resp.RoleAdminListResp;
import com.github.pagehelper.Page;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
@Mapper
public interface RoleMapper {

	/**
	 * 获取详情
	 *
	 * @author: lyb
	 * @date: 2023/4/28 2:54 PM
	 */
	RoleEntity findOneByRoleId(long roleId);

	/**
	 * 获取角色列表
	 *
	 * @author: lyb
	 * @date: 2023/4/28 2:55 PM
	 */
	Page<RoleAdminListResp> findAllList(SelectRoleReq req);

	/**
	 * 添加数据
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	int insert(RoleEntity record);

	/**
	 * 修改数据
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	int updateByRoleId(RoleEntity record);

	/**
	 * 删除
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	int deleteByRoleId(long roleId);
}
