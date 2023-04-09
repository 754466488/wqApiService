package com.dzw.micro.wq.enums;

import com.dzw.micro.wq.application.domain.req.Resp;
import com.dzw.micro.wq.resp.SelectedResp;
import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * 常用状态
 *
 * @author: lyb
 * @date: 2023/4/5 21:06
 */
public enum BannerPositionEnum {

	TOP(1, "顶部轮播"),
	LEFT(2, "左侧轮播图"),
	FOOT(3, "底部轮播图");

	BannerPositionEnum(int code, String name) {
		this.code = code;
		this.name = name;
	}

	@Getter
	private int code;
	@Getter
	private String name;

	/**
	 * 根据code找名称
	 *
	 * @param code
	 * @return
	 */
	public static String getNameByCode(int code) {
		for (BannerPositionEnum enableStatusEnum : BannerPositionEnum.values()) {
			if (enableStatusEnum.getCode() == code) {
				return enableStatusEnum.getName();
			}
		}
		return StringUtils.EMPTY;
	}

	public static Resp<List<SelectedResp>> bannerPositionList() {
		List<SelectedResp> selectedDTOList = Lists.newArrayList();
		for (BannerPositionEnum bannerPositionEnum : BannerPositionEnum.values()) {
			SelectedResp selectedResp = new SelectedResp();
			selectedResp.setCode(bannerPositionEnum.getCode());
			selectedResp.setName(bannerPositionEnum.getName());
			selectedDTOList.add(selectedResp);
		}
		return Resp.success(selectedDTOList);
	}
}
