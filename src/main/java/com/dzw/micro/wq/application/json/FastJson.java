package com.dzw.micro.wq.application.json;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.DoubleSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;


/**
 * <b>json解析库fastjson的简单封装</b><br>
 *
 * @author zhoutao
 * @thread-safe
 * @since 2015-11-2
 */
public final class FastJson {
	//不忽略NULL值,一般在转json字符串要落地NoSql的时候使用，否则会存在null值的字段不存在的情况
	private static SerializerFeature[] WRITE_NULL_VALUE_SERIALIZER_FEATURES = new SerializerFeature[]{
			SerializerFeature.WriteMapNullValue,
			SerializerFeature.WriteNullStringAsEmpty,
			SerializerFeature.WriteNullNumberAsZero,
			SerializerFeature.WriteNullListAsEmpty,
			SerializerFeature.WriteNullBooleanAsFalse
	};
	private static SerializeConfig serializeConfig = SerializeConfig.getGlobalInstance();
	static {
		serializeConfig.put(Double.class, new DoubleSerializer("#.########"));
	}

	private FastJson() {
	}

	/**
	 * json字符串到对象的转换
	 *
	 * @param jsonStr
	 * @return
	 * @throws JsonException
	 */
	public static <T> T jsonStr2Object(String jsonStr, Class<T> valueType) {
		try {
			return JSON.parseObject(jsonStr, valueType);
		} catch (Exception e) {
			throw new JsonException("FastJson.jsonStr2Object error.", e);
		}
	}

	/**
	 * 对象到json字符串的转换
	 *
	 * @param obj
	 * @return
	 * @throws JsonException
	 */
	public static String object2JsonStr(Object obj) {
		try {
			return JSON.toJSONString(obj);
		} catch (Exception e) {
			throw new JsonException("FastJson.object2JsonStr error.", e);
		}
	}

	/**
	 * 对象到json字符串的转换
	 *
	 * @param obj
	 * @return
	 * @throws JsonException
	 */
	public static String object2JsonStrUseNullValue(Object obj) {
		try {
			return JSON.toJSONString(obj, WRITE_NULL_VALUE_SERIALIZER_FEATURES);
		} catch (Exception e) {
			throw new JsonException("FastJson.object2JsonStrUseNullValue error.", e);
		}
	}

}
