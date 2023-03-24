package com.dzw.micro.wq.application.config.concurrent;

import com.dzw.micro.wq.application.domain.req.BaseReq;
import com.dzw.micro.wq.application.domain.req.Resp;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

@Api(tags = {"并发Api"})
@RestController
@RequestMapping(value = "/api/concurrent")
public class ConcurrentApi {
	@Autowired
	private ApplicationContext applicationContext;

	@ApiOperation(value = "统计线程池使用情况", notes = "")
	@PostMapping(path = "/threadPool/stat")
	@SuppressWarnings("unchecked")
	public Resp statThreadPool(@Valid BaseReq req, @ApiIgnore BindingResult bindingResult) {
		Map<String, ThreadPoolExecutor> handlerBeanMap = applicationContext.getBeansOfType(ThreadPoolExecutor.class);
		List<Map<String, Object>> statList = Lists.newArrayList();
		for (Map.Entry<String, ThreadPoolExecutor> entry : handlerBeanMap.entrySet()) {
			String k = entry.getKey();
			ThreadPoolExecutor v = entry.getValue();
			Map<String, Object> statMap = Maps.newLinkedHashMap();
			statMap.put("Name", k);
			statMap.put("PoolSize", v.getPoolSize());
			statMap.put("CorePoolSize", v.getCorePoolSize());
			statMap.put("MaximumPoolSize", v.getMaximumPoolSize());
			statMap.put("LargestPoolSize", v.getLargestPoolSize());
			statMap.put("ActiveCount", v.getActiveCount());
			statMap.put("TaskCount", v.getTaskCount());
			statMap.put("CompletedTaskCount", v.getCompletedTaskCount());
			statMap.put("QueueSize", v.getQueue().size());
			statList.add(statMap);
		}
		return Resp.success(statList);
	}

	@ApiOperation(value = "修改线程池参数", notes = "")
	@PostMapping(path = "/threadPool/update")
	@SuppressWarnings("unchecked")
	public Resp updateThreadPool(@Valid ThreadPoolParamReq req, @ApiIgnore BindingResult bindingResult) {
		Map<String, ThreadPoolExecutor> handlerBeanMap = applicationContext.getBeansOfType(ThreadPoolExecutor.class);
		ThreadPoolExecutor threadPoolExecutor = handlerBeanMap.get(req.getThreadPoolName());
		if (threadPoolExecutor == null) {
			return Resp.error("The thread pool was not found");
		}
		if (req.getCorePoolSize() > 0) {
			threadPoolExecutor.setCorePoolSize(req.getCorePoolSize());
		}
		if (req.getMaximumPoolSize() > 0 && req.getMaximumPoolSize() >= req.getCorePoolSize()) {
			threadPoolExecutor.setMaximumPoolSize(req.getMaximumPoolSize());
		}
		return Resp.success();
	}

	@Data
	@ApiModel
	static class ThreadPoolParamReq extends BaseReq {
		@NotBlank
		@ApiModelProperty(value = "线程池名称", required = true)
		private String threadPoolName;
		@ApiModelProperty(value = "corePoolSize")
		private int corePoolSize;
		@ApiModelProperty(value = "maximumPoolSize")
		private int maximumPoolSize;
	}
}
