package com.dzw.micro.wq.application.domain.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * api基础请求对象
 * 封装系统级参数,具体的校验可以放到ApiInterceptor中这里不做强制JSR注解校验
 *
 * @author zhoutao
 * @date 2020/11/26
 */
@Data
@ApiModel
public class BaseReq implements Req {
	@ApiModelProperty(value = "token|公共参数")
	private String token;
	/**
	 * see ApiAppIdHandler.java
	 */
	@ApiModelProperty(value = "调用方id,服务分配|公共参数")
	private String appId;
	/**
	 * 时间戳和nonce结合防重放
	 * 1 timestamp进行30秒内第一步验证
	 * 2 nonce判断请求是否唯一，调用方nonce生成可使用uuid或md5(timestamp+random(0,1000))
	 */
	@ApiModelProperty(value = "Unix时间戳如:1610421482|公共参数")
	private String timestamp;
	@ApiModelProperty(value = "随机字符串不长于32位如:ibuaiVcKdpRxkhJA|公共参数")
	private String nonce;
	/**
	 * 对除sign之外的其他参数签名(md5或rsa,还需结合给调用方分配的appSecret做盐)防篡改
	 */
	@ApiModelProperty(value = "sign|公共参数")
	private String sign;
	@ApiModelProperty(value = "os|公共参数")
	private String os;
	@ApiModelProperty(value = "version|公共参数")
	private String version;
}
