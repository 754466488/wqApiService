package com.dzw.micro.wq.model;

import lombok.Data;

/**
 * 资源配置表
 *
 * @author: lyb
 * @date: 2023/4/27 20:16
 */
@Data
public class ResourceEntity {
	private long resourceId;
	private long pid;
	private String name;
	private String url;
	private String icon;
	private int req;
	private int type;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
}
