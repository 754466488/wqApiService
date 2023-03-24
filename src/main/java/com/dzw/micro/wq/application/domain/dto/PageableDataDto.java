package com.dzw.micro.wq.application.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 可分页对象 Resp.success(PageableDataDto<UserDto>)
 *
 * @author zhoutao
 * @date 2020/11/26
 */
@Data
@ApiModel
public class PageableDataDto<T> {
	@ApiModelProperty(value = "记录总数")
	private long totalSize;

	@ApiModelProperty(value = "数据列表")
	private List<T> dtoList;
}
