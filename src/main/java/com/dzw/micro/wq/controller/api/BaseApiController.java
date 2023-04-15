package com.dzw.micro.wq.controller.api;

import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.base.UploadFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@Api(tags = {"基础服务API"})
@RestController
@RequestMapping(value = "/api/base/")
@Slf4j
public class BaseApiController {
	@Autowired
	private UploadFileService uploadFileService;

	@ApiOperation(value = "文件上传", notes = "")
	@PostMapping("/file/uploadFile")
	public Resp<UploadImgResp> uploadFile(@Valid UploadApiReq req, @ApiIgnore BindingResult bindingResult) {
		UploadReq req2 = new UploadReq();
		req2.setFile(req.getFile());
		req2.setFileType(req.getFileType());
		return uploadFileService.uploadFile(req2);
	}

	@Data
	public class UploadApiReq extends BaseReq {
		@ApiModelProperty(value = "文件")
		private MultipartFile file;
		@ApiModelProperty(value = "1:图片 2：视频", required = true)
		private int fileType;
	}
}
