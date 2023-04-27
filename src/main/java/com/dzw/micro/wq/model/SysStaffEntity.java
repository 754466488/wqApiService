package com.dzw.micro.wq.model;

import lombok.Data;

@Data
public class SysStaffEntity {
	private Long staffId;
	private String name;
	private String userName;
	private String pass;
	private String phone;
	private String idCard;
	private String positions;
	private Byte status;
	private String createTime;
	private String createUser;
	private String updateTime;
	private String updateUser;
}