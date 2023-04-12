package com.dzw.micro.wq.service.base;

import com.dzw.micro.wq.application.domain.constant.MixedConstant;
import com.dzw.micro.wq.application.domain.id.DistributedId;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.application.log.Log;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
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
public class UploadFileService {
	@Autowired
	private OBSService obsService;
	private static final List<String> imgTypeList = Lists.newArrayList("jpg", "jpeg", "gif", "png");
	private static final List<String> videoTypeList = Lists.newArrayList("mp4");

	public Resp uploadFile(UploadReq req) {
		MultipartFile file = req.getFile();
		int fileType = req.getFileType();
		if (file == null || file.isEmpty()) {
			return Resp.error("请先选择图片");
		}
		if (fileType == 1 && file.getSize() > MixedConstant.IMG_FILE_MAX_SIZE) {
			return Resp.error("上传的图片不能大于5MB");
		}
		if (fileType == 2 && file.getSize() > MixedConstant.VIDEO_FILE_MAX_SIZE) {
			return Resp.error("上传的视频不能大于200MB");
		}
		final String filename = file.getOriginalFilename();
		final String fileExtension = FileUtil.getFileExtension(filename);
		if (fileType == 1 && !imgTypeList.contains(fileExtension.toLowerCase())) {
			return Resp.error("图片格式只支持jpg/jpeg/gif/png");
		}
		if (fileType == 2 && !videoTypeList.contains(fileExtension.toLowerCase())) {
			return Resp.error("视频格式只支持mp4");
		}
		String module = "base/image";
		if (fileType == 2) {
			module = "base/video";
		}
		UploadImgResp uploadImgResp = new UploadImgResp();
		String imgName = DistributedId.get().toString();
		String imgUrl;
		try {
			imgUrl = obsService.upload(module, imgName, file);
			uploadImgResp.setUrl(imgUrl);
		} catch (Throwable e) {
			Log.error("uploadImage error", e);
			return Resp.error("上传文件失败");
		}
		return Resp.success(uploadImgResp);
	}
}
