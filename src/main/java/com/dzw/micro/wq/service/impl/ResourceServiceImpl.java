package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.mapper.ResourceMapper;
import com.dzw.micro.wq.resp.ResourceResp;
import com.dzw.micro.wq.service.IResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/28
 */
@Service
public class ResourceServiceImpl implements IResourceService {
	@Autowired
	private ResourceMapper resourceMapper;

	@Override
	public Resp<List<ResourceResp>> list() {
		List<ResourceResp> list = resourceMapper.findAllList();
		return Resp.success(list);
	}
}
