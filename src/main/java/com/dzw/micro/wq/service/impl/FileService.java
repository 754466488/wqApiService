package com.dzw.micro.wq.service.impl;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.domain.constant.MixedConstant;
import com.dzw.micro.wq.application.domain.id.DistributedId;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.req.UploadReq;
import com.dzw.micro.wq.resp.UploadImgResp;
import com.dzw.micro.wq.service.IFileService;
import com.google.common.collect.Lists;
import com.vip.vjtools.vjkit.io.FileUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

/**
 * description
 *
 * @author lyb
 * @date created in 2023/4/15
 */
@Service
public class FileService implements IFileService {
	@Value(value = "${file.resourceLocation}")
	private String resourceLocation;
	private static final List<String> imgTypeList = Lists.newArrayList("jpg", "jpeg", "gif", "png");
	private static final List<String> videoTypeList = Lists.newArrayList("mp4", "avi", "rmvb");

	@Override
	public Resp upload(UploadReq req) {
		MultipartFile file = req.getFile();
		int fileType = req.getFileType();
		String pathUrl = "";
		String fileHome = new File(System.getProperty("user.dir") + resourceLocation).getAbsolutePath();
		if (file == null || file.isEmpty()) {
			return Resp.error("请先选择图片");
		}
		if (fileType == 1 && file.getSize() > MixedConstant.IMG_FILE_MAX_SIZE) {
			return Resp.error("上传的图片不能大于5MB");
		}
		if (fileType == 2 && file.getSize() > MixedConstant.VIDEO_FILE_MAX_SIZE) {
			return Resp.error("上传的视频不能大于100MB");
		}
		try {
			String filename = file.getOriginalFilename();
			String fileExtension = FileUtil.getFileExtension(filename);
			if (fileType == 1 && !imgTypeList.contains(fileExtension.toLowerCase())) {
				return Resp.error("图片格式只支持jpg/jpeg/gif/png");
			}
			if (fileType == 2 && !videoTypeList.contains(fileExtension.toLowerCase())) {
				return Resp.error("视频格式只支持mp4");
			}
			String pathBody = File.separator + "images" + File.separator;
			if (fileType == 2) {
				pathBody = File.separator + "video" + File.separator;
			}
			String newFile = DistributedId.get().toString();
			String newFileName = newFile + "." + fileExtension;
			String pathName = fileHome + pathBody + newFileName;
			File newfile = new File(pathName);
			if (!newfile.getParentFile().exists()) {
				newfile.getParentFile().mkdirs();
			}
			file.transferTo(newfile);
			pathUrl = pathBody + newFileName;
			UploadImgResp uploadImgResp = new UploadImgResp();
			uploadImgResp.setUrl(pathUrl);
			return Resp.success(uploadImgResp);
		} catch (Exception e) {
			return Resp.error("上传文件失败,原因：" + e.getMessage());
		}
	}
}
