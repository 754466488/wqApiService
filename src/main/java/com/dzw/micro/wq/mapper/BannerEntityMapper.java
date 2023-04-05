package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.BannerEntity;
import com.dzw.micro.wq.resp.BannerListResp;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;
import org.mapstruct.Mapper;

@Mapper
public interface BannerEntityMapper {
	Page<BannerListResp> findList(@Param("status") int status, @Param("type") int type);

	BannerEntity findOneById(Long id);

	int deleteById(Long id);

	int insert(BannerEntity record);

	int updateById(BannerEntity record);
}