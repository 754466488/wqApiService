package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.NewsEntity;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.resp.NewsListResp;
import com.github.pagehelper.Page;

public interface NewsEntityMapper {
	Page<NewsListResp> findList(SelectNewsReq req);

	NewsEntity findOneById(Long id);

	int insert(NewsEntity record);

	int updateById(NewsEntity record);

}