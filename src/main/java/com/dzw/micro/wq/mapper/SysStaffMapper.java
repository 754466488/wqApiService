package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.SysStaffEntity;
import com.dzw.micro.wq.req.SelectStaffReq;
import com.dzw.micro.wq.resp.SysStaffListResp;
import com.github.pagehelper.Page;
import org.mapstruct.Mapper;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
@Mapper
public interface SysStaffMapper {
	Page<SysStaffListResp> findList(SelectStaffReq req);

	SysStaffEntity findOneByUserName(String userName);

	SysStaffEntity findOneByStaffId(long staffId);

	int update(SysStaffEntity entity);

	int insert(SysStaffEntity entity);

	int deleteByStaffId(long staffId);
}