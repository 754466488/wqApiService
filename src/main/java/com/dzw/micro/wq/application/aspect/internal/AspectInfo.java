package com.dzw.micro.wq.application.aspect.internal;

import lombok.Data;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.annotation.Annotation;

/**
 * 切面信息
 *
 * @author lyb
 * @date 2020/11/26
 */
@Data
public class AspectInfo {
	private Annotation[] methodAnnotations;
	private Object[] methodParam;
	private String methodName;
	private Class classType;
	private AspectTypeEnum aspectTypeEnum;
	private StringBuilder logSB;
	private long createTime;

	/**
	 * 切面参数数组中获取指定类型参数
	 *
	 * @return
	 */
	public <T> T findParam(Class<T> paramClass) {
		Object[] params = this.getMethodParam();
		if (ArrayUtils.isNotEmpty(params)) {
			for (Object param : params) {
				if (paramClass.isAssignableFrom(param.getClass())) {
					return paramClass.cast(param);
				}
			}
		}
		return null;
	}
}
