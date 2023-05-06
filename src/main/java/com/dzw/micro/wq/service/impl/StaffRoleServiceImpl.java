package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.mapper.StaffRoleMapper;
import com.dzw.micro.wq.model.StaffRoleEntity;
import com.dzw.micro.wq.req.BindRoleReq;
import com.dzw.micro.wq.service.IStaffRoleService;
import com.google.common.base.Splitter;
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
public class StaffRoleServiceImpl implements IStaffRoleService {
	@Autowired
	private StaffRoleMapper staffRoleMapper;

	@Override
	public Resp<List<Long>> getBindList(long staffId) {
		List<Long> longList = Lists.newArrayList();
		List<StaffRoleEntity> list = staffRoleMapper.findListByStaffId(staffId);
		if (CollectionUtils.isNotEmpty(list)) {
			longList = list.stream().map(StaffRoleEntity::getStaffId).collect(Collectors.toList());
		}
		return Resp.success(longList);
	}

	@Override
	public Resp bind(BindRoleReq req) {
		staffRoleMapper.deleteByStaffId(req.getStaffId());
		String roleIds = req.getRoleIds();
		List<String> ListRoleId = Splitter.on(",").splitToList(roleIds);
		for (String str : ListRoleId
		) {
			StaffRoleEntity entity = new StaffRoleEntity();
			entity.setStaffId(req.getStaffId());
			entity.setRoleId(Long.parseLong(str));
			staffRoleMapper.insert(entity);
		}
		return Resp.success();
	}
}
