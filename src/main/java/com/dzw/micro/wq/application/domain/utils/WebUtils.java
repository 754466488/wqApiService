package com.dzw.micro.wq.application.domain.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class WebUtils {
	private WebUtils() {
	}

	/**
	 * 获取当前请求的地址
	 *
	 * @return
	 */
	public static final String getRequestURI() {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return httpServletRequest == null ? "" : httpServletRequest.getRequestURI();
	}

	/**
	 * 获取当前请求的头信息
	 *
	 * @return
	 */
	public static final String getRequestHeader(String header) {
		HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		return httpServletRequest == null ? "" : httpServletRequest.getHeader(header);
	}
}
