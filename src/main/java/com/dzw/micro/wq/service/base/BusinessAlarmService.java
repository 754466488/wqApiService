package com.dzw.micro.wq.service.base;

import com.alibaba.fastjson.JSONObject;
import com.dzw.micro.wq.Application;
import com.dzw.micro.wq.application.httpclient.HttpClientUtils;
import com.dzw.micro.wq.application.utils.ThrowableUtils;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>Description: </p>
 *
 * @author: by lyb
 * @date: 2021/4/12  4:03 PM
 * @version: 1.0
 */
@Service
public class BusinessAlarmService {

	@Autowired
	private HttpClientUtils alarmHttpClient;
	//发送到线上群里
	public final static String FEISHU_URL = "https://open.feishu.cn/open-apis/bot/v2/hook/4da7dbfa-e545-42ed-adee-40d55f7a7901";
	//发送的超时时间
	public static final int TIMEOUT = 30000;
	@Autowired
	private ThreadPoolExecutor commonExecutor;

	public void sendAlarm(String message, Throwable throwable) {
		if (Application.isDev()) {
			return;
		}

		StringBuilder sb = new StringBuilder();
		sb.append("系统：").append(Application.NAME).append("\r\n");
		sb.append("当前环境：").append(Application.ENV).append("\r\n");
		String traceId = MDC.get("X-B3-TraceId");
		if (StringUtils.isNotBlank(traceId)) {
			sb.append("TraceId：").append(traceId).append("\r\n");
		}
		sb.append("报警内容：").append(message).append("\r\n");
		if (throwable != null) {
			sb.append("异常栈：").append(ThrowableUtils.getStackTraceAsSimpleString(throwable));
		}

		commonExecutor.execute(() -> {
			alarmHttpClient.httpCallPostJson(FEISHU_URL, buildJsonParams(sb.toString()), TIMEOUT);
		});

	}

	public void sendAlarm(String message) {
		if (Application.isDev()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("系统：").append(Application.NAME).append("\r\n");
		sb.append("当前环境：").append(Application.ENV).append("\r\n");
		String traceId = MDC.get("X-B3-TraceId");
		if (StringUtils.isNotBlank(traceId)) {
			sb.append("TraceId：").append(traceId).append("\r\n");
		}
		sb.append("报警内容：").append(message).append("\r\n");

		commonExecutor.execute(() -> {
			alarmHttpClient.httpCallPostJson(FEISHU_URL, buildJsonParams(sb.toString()), TIMEOUT);
		});
	}

	private String buildJsonParams(String msgStr) {
		Map<String, Object> contentMap = Maps.newHashMapWithExpectedSize(1);
		contentMap.put("text", msgStr);

		Map<String, Object> messageMap = Maps.newHashMapWithExpectedSize(2);
		messageMap.put("msg_type", "text");
		messageMap.put("content", contentMap);
		return JSONObject.toJSONString(messageMap);
	}
}
