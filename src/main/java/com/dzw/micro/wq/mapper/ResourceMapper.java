package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.ResourceEntity;
import com.dzw.micro.wq.resp.ResourceResp;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/27
 */
@Mapper
public interface ResourceMapper {
	List<ResourceResp> findAllList();
}
