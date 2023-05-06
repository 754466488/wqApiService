package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.mapper.FuncPermissionMapper;
import com.dzw.micro.wq.model.FuncPermissionEntity;
import com.dzw.micro.wq.resp.RoleAdminListResp;
import com.dzw.micro.wq.service.IFuncPermissionService;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
@Service
public class FuncPermissionServiceImpl implements IFuncPermissionService {
	@Autowired
	private FuncPermissionMapper funcPermissionMapper;

	@Override
	public Resp<List<RoleAdminListResp>> bindList(long roleId) {
		List<Long> longList = Lists.newArrayList();
		List<FuncPermissionEntity> list = funcPermissionMapper.findListByRoleId(roleId);
		if (CollectionUtils.isEmpty(list)) {
			return Resp.success(longList);
		}
		longList = list.stream().map(FuncPermissionEntity::getResourceId).collect(Collectors.toList());
		return Resp.success(longList);
	}
}
