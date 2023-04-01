package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.BannerEntity;

public interface BannerEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BannerEntity record);

    int insertSelective(BannerEntity record);

    BannerEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BannerEntity record);

    int updateByPrimaryKey(BannerEntity record);
}