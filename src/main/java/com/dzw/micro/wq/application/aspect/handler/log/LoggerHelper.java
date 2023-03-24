package com.dzw.micro.wq.application.aspect.handler.log;

import com.dzw.micro.wq.application.domain.constant.SymbolConstant;
import com.dzw.micro.wq.application.json.FastJson;
import com.dzw.micro.wq.application.log.Log;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 切面日志打印帮助类
 *
 * @author lyb
 * @date 2020/11/26
 */
@Component
public class LoggerHelper {
	public final String fetchName(String classSimpleName, String methodName) {
		StringBuilder sb = new StringBuilder();
		sb.append(classSimpleName).append(SymbolConstant.PERIOD).append(methodName);
		return sb.toString();
	}

	/**
	 * 参数数组转换为日志输出的字符串
	 *
	 * @param params
	 * @return
	 */
	public final String fetchParam(Object[] params) {
		if (ArrayUtils.isEmpty(params)) {
			return SymbolConstant.EMPTY;
		}
		return Arrays.stream(params)
				.map(param -> obj2String(param))
				.filter(Objects::nonNull)
				.collect(Collectors.toList())
				.toString();
	}

	/**
	 * 返回对象转换为日志输出的字符串
	 *
	 * @return
	 */
	public final String fetchResult(Object result) {
		return obj2String(result);
	}

	private final String obj2String(Object obj) {
		boolean canPrint = (obj != null) &&
				!(obj instanceof BindingResult) &&
				!(obj instanceof ServletRequest) &&
				!(obj instanceof ServletResponse) &&
				!(obj instanceof MultipartFile);
		if (!canPrint) {
			return null;
		}
		String str = null;
		if (obj instanceof String) {
			str = (String) obj;
		} else {
			try {
				str = FastJson.object2JsonStr(obj);
			} catch (Exception e) {
				Log.error("LoggerHelper.obj2String[obj=" + obj + "] error", e);
			}
		}

		return str;
	}
}
