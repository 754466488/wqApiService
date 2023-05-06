package com.dzw.micro.wq.resp;

import lombok.Data;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/5/6
 */
@Data
public class RoleAdminListResp {
	private long roleId;
	private String roleName;
	private String roleDesc;
	private String createTime;
	private String createUser;
}
