package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.NewsEntity;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.resp.NewsApiListResp;
import com.dzw.micro.wq.resp.NewsListResp;
import com.github.pagehelper.Page;

import java.util.List;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
public interface NewsEntityMapper {
	Page<NewsListResp> findList(SelectNewsReq req);

	Page<NewsListResp> findContentList(SelectNewsReq req);

	NewsEntity findOneById(Long id);

	int insert(NewsEntity record);

	int updateById(NewsEntity record);

	int addClickNumById(long id);

	List<NewsApiListResp> findPageHomeNewsList();

}