package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.NewsEntity;
import com.dzw.micro.wq.req.SelectNewsReq;
import com.dzw.micro.wq.resp.NewsApiListResp;
import com.dzw.micro.wq.resp.NewsListResp;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
public interface NewsMapper {
	Page<NewsListResp> findList(SelectNewsReq req);

	Page<NewsApiListResp> findContentList(SelectNewsReq req);

	NewsEntity findOneById(Long id);

	NewsEntity findUpOne(@Param("id") Long id, @Param("menuId") Long menuId);

	NewsEntity findDownOne(@Param("id") Long id, @Param("menuId") Long menuId);

	int insert(NewsEntity record);

	int updateById(NewsEntity record);

	int addClickNumById(long id);

	List<NewsApiListResp> findPageHomeNewsList();

	boolean existsTop(Integer isTop);
}