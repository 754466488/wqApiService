package com.dzw.micro.wq.model;

import lombok.Data;
import org.joda.time.DateTime;

@Data
public class MenuEntity {
	private Long id;

	private String name;

	private Long pid;

	private Integer level;

	private Integer req;

	private Integer status;

	private String createTime;

	private String createUser;
}