package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.utils.BeanUtils;
import com.dzw.micro.wq.application.utils.DateUtils;
import com.dzw.micro.wq.mapper.FuncPermissionMapper;
import com.dzw.micro.wq.mapper.ResourceMapper;
import com.dzw.micro.wq.mapper.StaffRoleMapper;
import com.dzw.micro.wq.mapper.SysStaffMapper;
import com.dzw.micro.wq.model.FuncPermissionEntity;
import com.dzw.micro.wq.model.StaffRoleEntity;
import com.dzw.micro.wq.model.SysStaffEntity;
import com.dzw.micro.wq.req.LoginReq;
import com.dzw.micro.wq.req.SaveStaffReq;
import com.dzw.micro.wq.req.SelectStaffReq;
import com.dzw.micro.wq.resp.PageableDataResp;
import com.dzw.micro.wq.resp.ResourceResp;
import com.dzw.micro.wq.resp.SysStaffListResp;
import com.dzw.micro.wq.resp.UserInfoResp;
import com.dzw.micro.wq.service.ISysStaffService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/9
 */
@Service
public class SysStaffServiceImpl implements ISysStaffService {
	@Autowired
	private SysStaffMapper sysStaffMapper;
	@Autowired
	private StaffRoleMapper staffRoleMapper;
	@Autowired
	private FuncPermissionMapper funcPermissionMapper;
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Resp<PageableDataResp<SysStaffListResp>> list(SelectStaffReq req) {
		PageableDataResp<SysStaffListResp> pageableDataResp = new PageableDataResp<>();
		PageHelper.startPage(req.getPageNo(), req.getPageSize());
		Page<SysStaffListResp> respPage = sysStaffMapper.findList(req);
		pageableDataResp.setTotalSize(respPage.getTotal());
		pageableDataResp.setDtoList(respPage.getResult());
		return Resp.success(pageableDataResp);
	}

	@Override
	public Resp<UserInfoResp> login(LoginReq req) {
		SysStaffEntity sysStaffEntity = sysStaffMapper.findOneByUserName(req.getUserName());
		if (Objects.isNull(sysStaffEntity)) {
			return Resp.error("该用户不存在");
		}
		if (!Objects.equals(req.getPassword(), sysStaffEntity.getPass())) {
			return Resp.error("密码错误");
		}
		List<StaffRoleEntity> staffRoleList = staffRoleMapper.findListByStaffId(sysStaffEntity.getStaffId());
		if (CollectionUtils.isEmpty(staffRoleList)) {
			return Resp.error("用户还未分配权限");
		}
		List<Long> roleIds = staffRoleList.stream().map(StaffRoleEntity::getRoleId).collect(Collectors.toList());
		List<FuncPermissionEntity> funcPermissionList = funcPermissionMapper.findListByRoleIds(roleIds);
		List<ResourceResp> respList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(funcPermissionList)) {
			List<Long> resourceIds = funcPermissionList.stream().map(FuncPermissionEntity::getResourceId).collect(Collectors.toList());
			respList = resourceMapper.findListByResourceIds(resourceIds);
		}

		UserInfoResp resp = new UserInfoResp();
		resp.setStaffId(sysStaffEntity.getStaffId());
		resp.setName(sysStaffEntity.getName());
		resp.setUserName(sysStaffEntity.getUserName());
		resp.setResourceResp(respList);
		return Resp.success(resp);
	}

	@Override
	public Resp save(SaveStaffReq req) {
		if (Objects.isNull(req.getUserId())) {
			SysStaffEntity staffEntity = sysStaffMapper.findOneByUserName(req.getName());
			if (Objects.nonNull(staffEntity)) {
				return Resp.error("此用户已经存在");
			}
			SysStaffEntity entity = new SysStaffEntity();
			BeanUtils.copyProperties(entity, req);
			entity.setStaffId(0L);
			entity.setUserName(req.getRegisterUserName());
			entity.setCreateTime(DateUtils.currentTimeSecond());
			entity.setCreateUser(req.getUserName());
			sysStaffMapper.insert(entity);
		} else {
			SysStaffEntity staffEntity = sysStaffMapper.findOneByStaffId(req.getUserId());
			if (Objects.nonNull(staffEntity)) {
				BeanUtils.copyProperties(staffEntity, req);
				staffEntity.setStaffId(req.getUserId());
				staffEntity.setUserName(req.getRegisterUserName());
				staffEntity.setUpdateUser(req.getUserName());
				staffEntity.setUpdateTime(DateUtils.currentTimeSecond());
				sysStaffMapper.update(staffEntity);
			}
		}
		return Resp.success();
	}

	@Override
	public Resp delete(long staffId) {
		sysStaffMapper.deleteByStaffId(staffId);
		staffRoleMapper.deleteByStaffId(staffId);
		return Resp.success();
	}
}
