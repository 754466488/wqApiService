package com.dzw.micro.wq.req;

import com.dzw.micro.wq.application.domain.req.BaseReq;
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
public class UploadReq extends BaseReq {
	@ApiModelProperty(value = "文件")
	private MultipartFile file;
	@ApiModelProperty(value = "图片类型(1:身份证，2：驾驶证，3：行驶证，4道路运输证，99 其它)", required = true)
	private int imgType;
}

