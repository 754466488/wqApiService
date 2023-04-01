package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.NewsEntity;

public interface NewsEntityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(NewsEntity record);

    int insertSelective(NewsEntity record);

    NewsEntity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(NewsEntity record);

    int updateByPrimaryKey(NewsEntity record);
}