package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.base.UploadFileService;
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

@Api(tags = {"基础服务API"})
@RestController
@RequestMapping(value = "/admin/base/")
@Slf4j
public class BaseAdminApiController {
	@Autowired
	private UploadFileService uploadFileService;

	@ApiOperation(value = "后台图片上传", notes = "")
	@PostMapping("/file/uploadFile")
	public Resp<UploadImgResp> uploadFile(@Valid UploadReq req, @ApiIgnore BindingResult bindingResult) {
		return uploadFileService.uploadFile(req);
	}
}
