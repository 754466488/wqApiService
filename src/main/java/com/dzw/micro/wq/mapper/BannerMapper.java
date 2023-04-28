package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.BannerEntity;
import com.dzw.micro.wq.resp.BannerApiListResp;
import com.dzw.micro.wq.resp.BannerListResp;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
@Mapper
public interface BannerMapper {
	List<BannerApiListResp> findPageHomeList();

	Page<BannerListResp> findPageList(@Param("status") Integer status, @Param("type") Integer type);

	BannerEntity findOneById(Long id);

	int deleteById(Long id);

	int insert(BannerEntity record);

	int updateById(BannerEntity record);
}