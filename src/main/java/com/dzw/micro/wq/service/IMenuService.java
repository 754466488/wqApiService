package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.*;
import com.dzw.micro.wq.resp.MenuTreeResp;
import com.dzw.micro.wq.resp.MenusListResp;
import com.dzw.micro.wq.resp.PageableDataResp;

import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/6
 */
public interface IMenuService {
	Resp<PageableDataResp<MenusListResp>> findList(SelectMenuReq req);

	Resp save(SaveMenusReq req);

	Resp updateStatus(UpdateStatusReq req);

	Resp<List<MenuTreeResp>> treeList();
}
