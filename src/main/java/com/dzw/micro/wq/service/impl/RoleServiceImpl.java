package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.mapper.FuncPermissionMapper;
import com.dzw.micro.wq.mapper.RoleMapper;
import com.dzw.micro.wq.model.RoleEntity;
import com.dzw.micro.wq.req.SaveRoleReq;
import com.dzw.micro.wq.req.SelectRoleReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.RoleAdminListResp;
import com.dzw.micro.wq.service.IRoleService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
@Service
public class RoleServiceImpl implements IRoleService {
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private FuncPermissionMapper funcPermissionMapper;

	@Override
	public Resp<PageableDataResp<RoleAdminListResp>> list(SelectRoleReq req) {
		PageableDataResp<RoleAdminListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<RoleAdminListResp> respPage = roleMapper.findAllList(req);
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp save(SaveRoleReq req) {
		if (Objects.isNull(req.getRoleId())) {
			RoleEntity roleEntity = new RoleEntity();
			roleEntity.setRoleName(req.getRoleName());
			roleEntity.setRoleDesc(req.getRoleDesc());
			roleEntity.setCreateTime(DateUtils.currentTimeSecond());
			roleEntity.setCreateUser(req.getUserName());
			roleMapper.insert(roleEntity);
		} else {
			RoleEntity roleEntity = roleMapper.findOneByRoleId(req.getRoleId());
			if (Objects.nonNull(roleEntity)) {
				roleEntity.setRoleName(req.getRoleName());
				roleEntity.setRoleDesc(req.getRoleDesc());
				roleEntity.setUpdateTime(DateUtils.currentTimeSecond());
				roleEntity.setUpdateUser(req.getUserName());
				roleMapper.updateByRoleId(roleEntity);
			}
		}
		return Resp.success();
	}

	@Override
	public Resp delete(long roleId) {
		roleMapper.deleteByRoleId(roleId);
		funcPermissionMapper.deleteByRoleId(roleId);
		return Resp.success();
	}
}
