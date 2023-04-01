package com.dzw.micro.wq.model;

import lombok.Data;

@Data
public class MenuEntity {
	private Long id;

	private String name;

	private Long pid;

	private Integer level;

	private Integer req;

	private Integer status;

	private Long createTime;

	private String createUser;
}