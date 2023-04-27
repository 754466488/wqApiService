package com.dzw.micro.wq.model;

import lombok.Data;

/**
 * 角色表
 *
 * @author: lyb
 * @date: 2023/4/27 20:16
 */
@Data
public class RoleEntity {
	private long roleId;
	private String roleName;
	private String roleDesc;
	private long createTime;
	private String createUser;
	private long updateTime;
	private String updateUser;
}
