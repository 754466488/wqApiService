package com.dzw.micro.wq.req;

import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/5
 */
@Data
public class SelectNewsReq extends PageReq {
	private Integer status;
	private String beginTime;
	private String endTime;
	private Integer menu;
	private Integer isTop;
}
