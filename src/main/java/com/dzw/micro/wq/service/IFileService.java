package com.dzw.micro.wq.service;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/15
 */
public interface IFileService {
	public Resp upload(UploadReq req);
}
