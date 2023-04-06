package com.dzw.micro.wq.controller;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.UploadImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"后台-基础服务API"})
@RestController
@RequestMapping(value = "/admin/base/")
@Slf4j
public class BaseAdminApi {
	@Autowired
	private UploadImageService uploadImageService;

	@ApiOperation(value = "后台图片上传", notes = "")
	@PostMapping("/file/uploadImage")
	public Resp<UploadImgResp> uploadImg(@Valid UploadReq req, @ApiIgnore BindingResult bindingResult) {
		return uploadImageService.uploadImage(req);
	}
}
