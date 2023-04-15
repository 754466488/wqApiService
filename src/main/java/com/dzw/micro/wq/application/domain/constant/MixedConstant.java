package com.dzw.micro.wq.application.domain.constant;

/**
 * 应用中的常量定义
 *
 * @author: lyb
 * @date: 2023/4/10 22:08
 */
public interface MixedConstant {
	String TRACE_ID = "traceId";
	/**
	 * 上传附件最大文件大小 5M
	 */
	long IMG_FILE_MAX_SIZE = 5242880L;
	/**
	 * 上传附件最大文件大小 100M
	 */
	long VIDEO_FILE_MAX_SIZE = 104857600L;
}
