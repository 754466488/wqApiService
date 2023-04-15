package com.dzw.micro.wq.controller.admin;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.IFileService;
import com.dzw.micro.wq.service.base.UploadFileService;
import com.google.common.collect.Maps;
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
import java.util.HashMap;

@Api(tags = {"基础服务API"})
@RestController
@RequestMapping(value = "/admin/base/")
@Slf4j
public class BaseAdminApiController {
	@Autowired
	private IFileService fileService;

	@ApiOperation(value = "后台文件上传", notes = "")
	@PostMapping("/file/uploadFile")
	public Resp<UploadImgResp> uploadFile(@Valid UploadReq req, @ApiIgnore BindingResult bindingResult) {
		return fileService.upload(req);
	}

	@ApiOperation(value = "后台富文本编辑器上传", notes = "")
	@PostMapping("/file/textUploadFile")
	public HashMap textUploadFile(@Valid UploadReq req, @ApiIgnore BindingResult bindingResult) {
		Resp resp = fileService.upload(req);
		UploadImgResp data = (UploadImgResp) resp.getData();
		HashMap<Object, Object> map = Maps.newHashMap();
		map.put("errno", 0);
		map.put("data", data);
		return map;
	}
}
