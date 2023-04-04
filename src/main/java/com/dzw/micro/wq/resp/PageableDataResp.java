package com.dzw.micro.wq.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class PageableDataResp<T> {
	@ApiModelProperty(value = "记录总数")
	private long totalSize;

	@ApiModelProperty(value = "数据列表")
	private List<T> dtoList;
}
