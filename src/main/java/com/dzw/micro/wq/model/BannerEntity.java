package com.dzw.micro.wq.model;

import lombok.Data;

@Data
public class BannerEntity {
	private Long id;

	private String title;

	private String url;

	private Integer req;

	private Integer status;

	private String linkUrl;

	private Integer type;

	private Long createTime;

	private String createUser;

	private Long updateTime;

	private String updateUser;
}