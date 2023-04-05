package com.dzw.micro.wq.service;

import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.log.Log;
import com.dzw.micro.wq.application.utils.NumberDate;
import com.obs.services.ObsClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.io.IOException;

/**
 * obs服务
 *
 * @author: lyb
 * @date: 2023/4/5 23:00
 */
@Service
public class OBSService {
	private String bucketName = "obs-wq01";
	private String bucketLocation = "cn-north-4";
	private String publicAccessDomain = "https://ejiyun.obs.cn-north-4.myhuaweicloud.com";
	private String internalEndpoint = "obs.cn-north-4.myhuaweicloud.com";
	private String accessKey = "7G3ZFOCQSYI5CR4OGUGV";
	private String accessSecretKey = "COm4nECvxx92LgP2f3S3BuJzdLymIyuhpTv2Mkwp";

	private ObsClient obsClient;


	/**
	 * 上传文件
	 *
	 * @param module   - 模块标识,如果含有子模块用/分隔,比如shop/stock
	 * @param fileName - 文件名称
	 * @param file     - 原始文件流
	 * @return - 返回完整的访问路径
	 */
	public String upload(String module, String fileName, MultipartFile file) throws IOException {
		StringBuilder key = new StringBuilder();

		//顶级目录用环境标识避免覆盖
		key.append(Application.ENV);
		key.append("/");
		key.append(module);
		key.append("/");
		key.append(NumberDate.now().toString(NumberDate.TransType.MONTH));
		key.append("/");
		key.append(fileName);
		key.append(".");
		key.append(getExtensionName(file.getOriginalFilename()));

		//上传到华为云obs
		obsClient.putObject(bucketName, key.toString(), file.getInputStream());
		String publicAccessUrl = publicAccessDomain + "/" + key.toString();
		return publicAccessUrl;
	}

	/**
	 * 上传文件
	 *
	 * @param module - 模块标识,如果含有子模块用/分隔,比如shop/stock
	 * @param file   - 原始文件流
	 * @return - 返回完整的访问路径
	 */
	public String upload(String module, File file) throws IOException {
		StringBuilder key = new StringBuilder();

		//顶级目录用环境标识避免覆盖
		key.append(Application.ENV);
		key.append("/");
		key.append(module);
		key.append("/");
		key.append(NumberDate.now().toString(NumberDate.TransType.MONTH));
		key.append("/");
		key.append(file.getName());

		//上传到华为云oss
		obsClient.putObject(bucketName, key.toString(), file);
		String publicAccessUrl = publicAccessDomain + "/" + key.toString();
		return publicAccessUrl;
	}

	/**
	 * 获取文件扩展名
	 *
	 * @param filename
	 * @return
	 */
	public String getExtensionName(String filename) {
		if ((filename != null) && (filename.length() > 0)) {
			int dot = filename.lastIndexOf('.');
			if ((dot > -1) && (dot < (filename.length() - 1))) {
				return filename.substring(dot + 1);
			}
		}
		return filename;
	}

	@PostConstruct
	void init() {
		this.obsClient = new ObsClient(accessKey, accessSecretKey, internalEndpoint);
	}

	@PreDestroy
	void destory() {
		try {
			this.obsClient.close();
		} catch (IOException e) {
			Log.error("close obs client error", e);
		}
	}
}
