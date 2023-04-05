package com.dzw.micro.wq.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 常用状态
 *
 * @author: lyb
 * @date: 2023/4/5 21:06
 */
public enum EnableStatusEnum {

	ENABLE(0, "启用"),
	DISABLE(1, "停用"),
	WAIT_PUBLISH(2, "待发布");

	EnableStatusEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	@Getter
	private int code;
	@Getter
	private String name;

	public static boolean isEnable(int status) {
		return ENABLE.code == status;
	}

	public static boolean isDisable(int status) {
		return DISABLE.code == status;
	}

	public static boolean isWaitPublish(int status) {
		return WAIT_PUBLISH.code == status;
	}

	/**
	 * 根据code找名称
	 *
	 * @param code
	 * @return
	 */
	public static String getNameByCode(int code) {
		for (EnableStatusEnum enableStatusEnum : EnableStatusEnum.values()) {
			if (enableStatusEnum.getCode() == code) {
				return enableStatusEnum.getName();
			}
		}
		return StringUtils.EMPTY;
	}
}
