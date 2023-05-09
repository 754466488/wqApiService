package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseAdminReq;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

/**
 * description
 *
 * @author: lyb
 * @date: 2023/4/5 23:56
 */
@Data
@ApiModel
@ToString(callSuper = true, exclude = {"file"})
public class UploadReq extends BaseAdminReq {
	@ApiModelProperty(value = "文件")
	private MultipartFile file;
	@ApiModelProperty(value = "1:图片 2：视频", required = true)
	private int fileType;
}

