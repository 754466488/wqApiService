package com.dzw.micro.wq.model;

import lombok.Data;

/**
 * 关系表
 *
 * @author: lyb
 * @date: 2023/4/27 20:16
 */
@Data
public class FuncPermissionEntity {
	private long id;
	private long roleId;
	private long resourceId;
}