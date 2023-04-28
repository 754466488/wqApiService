package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.ResourceEntity;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/27
 */
public interface ResourceMapper {
	List<ResourceEntity> findAllList();
}
