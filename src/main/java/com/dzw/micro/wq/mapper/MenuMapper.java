package com.dzw.micro.wq.mapper;

import com.dzw.micro.wq.model.MenuEntity;
import com.dzw.micro.wq.resp.MenusListResp;
import com.github.pagehelper.Page;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/6 23:35
 */
@Mapper
public interface MenuMapper {
	/**
	 * 根据id获取对象
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:33
	 */
	MenuEntity findOneById(Long id);

	/**
	 * 获取分页列表
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	Page<MenusListResp> findList(Long pid);

	/**
	 * 获取所有数据
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	List<MenuEntity> findAll();

	/**
	 * 添加数据
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	int insert(MenuEntity record);

	/**
	 * 修改数据
	 *
	 * @author: lyb
	 * @date: 2023/4/6 23:34
	 */
	int updateById(MenuEntity record);
}