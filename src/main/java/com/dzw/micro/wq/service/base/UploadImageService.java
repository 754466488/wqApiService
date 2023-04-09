package com.dzw.micro.wq.service.base;

import com.dzw.micro.wq.application.domain.constant.MixedConstant;
import com.dzw.micro.wq.application.domain.id.DistributedId;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.log.Log;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.base.OBSService;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.io.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author: by lyb
 * @date: 2020/4/29  16:35
 * @version: 1.0
 */
@Service
@Slf4j
public class UploadImageService {
	@Autowired
	private OBSService obsService;
	private static final List<String> fileTypeList = Lists.newArrayList("jpg", "jpeg", "gif", "png");

	public Resp uploadImage(UploadReq req) {
		MultipartFile file = req.getFile();
		return uploadImage(file);
	}

	public Resp uploadImage(MultipartFile file) {
		if (file == null || file.isEmpty()) {
			return Resp.error("请先选择图片");
		}
		if (file.getSize() > MixedConstant.FILE_MAX_SIZE) {
			return Resp.error("上传的图片不能大于5MB");
		}
		final String filename = file.getOriginalFilename();
		final String fileExtension = FileUtil.getFileExtension(filename);
		if (!fileTypeList.contains(fileExtension.toLowerCase())) {
			return Resp.error("图片格式只支持jpg/jpeg/gif/png");
		}

		UploadImgResp uploadImgResp = new UploadImgResp();

		String module = "base/image";
		String imgName = DistributedId.get().toString();
		String imgUrl;
		try {
			imgUrl = obsService.upload(module, imgName, file);
			uploadImgResp.setImgUrl(imgUrl);
		} catch (Throwable e) {
			Log.error("uploadImage error", e);
			return Resp.error("上传图片失败");
		}
		return Resp.success(uploadImgResp);
	}
}
