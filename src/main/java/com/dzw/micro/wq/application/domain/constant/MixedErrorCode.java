package com.dzw.micro.wq.application.domain.constant;

/**
 * 业务错误码,只需要继承ErrorCode就可以
 * 400开头的是公共错误码，500开头系统错误，都在ErrorCode中保留使用，请勿覆盖
 * 一般无需新增定义，使用Resp.error("")即可，内部走的是ErrorCode("4011", "操作失败：%s")
 * <p>
 * <p>
 * 除非和调用方约定好了某些特定的code值，可在此类中新增自定义错误码
 */
public class MixedErrorCode extends ErrorCode {
	public static final ErrorCode MINI_DECRYPT_BAD_PADDING = new ErrorCode("4099", "解密用户信息失败,请重新授权");

	public MixedErrorCode(String code, String message) {
		super(code, message);
	}
}
