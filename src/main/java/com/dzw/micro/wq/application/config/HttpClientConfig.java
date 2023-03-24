package com.dzw.micro.wq.application.config;

import com.dzw.micro.wq.application.httpclient.HttpClientUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * http客户端配置类
 *
 * @author lyb
 * @date 2020/11/26
 */
@Configuration
public class HttpClientConfig {
	/**
	 * 业务模块公共httpClient,最好每个外部域名使用一个HttpClientUtils实例
	 *
	 * @return
	 */
	@Bean(name = "commonHttpClient")
	HttpClientUtils commonHttpClient() {
		// 默认3秒超时
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		return httpClientUtils;
	}

	/**
	 * 内部httpClient,业务模块不要使用
	 *
	 * @return
	 */
	@Bean(name = "internalHttpClient")
	HttpClientUtils internalHttpClient() {
		HttpClientUtils httpClientUtils = new HttpClientUtils();
		return httpClientUtils;
	}
}
