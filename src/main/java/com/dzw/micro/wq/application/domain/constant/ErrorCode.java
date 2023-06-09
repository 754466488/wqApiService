package com.dzw.micro.wq.application.domain.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 错误码
 * 400开头的是公共错误码
 * 其他如4100、4200等开头各模块自行定义
 * 500开头系统保留不要覆盖
 * 9{HttpStatusCode} 开头网关自身保留不要覆盖
 * 960开头给网关结合sentinel限流熔断保留不要覆盖
 * 只需要继承ErrorCode就可以
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ErrorCode implements Serializable {
    public static final ErrorCode PARAM_REQUIRED = new ErrorCode("4001", "参数：%s不能为空");
    public static final ErrorCode PARAM_INVALID = new ErrorCode("4002", "参数：%s格式无效");
    public static final ErrorCode SIGN = new ErrorCode("4003", "参数签名验证失败");
    public static final ErrorCode AUTHORITY = new ErrorCode("4004", "您没有权限访问该接口");
    public static final ErrorCode DECRYPT = new ErrorCode("4005", "请求数据解密失败");
    public static final ErrorCode INVALID_CALLER = new ErrorCode("4006", "无效的调用方");
    public static final ErrorCode UPLOAD_TYPE = new ErrorCode("4007", "您上传的文件类型不允许");
    public static final ErrorCode UPLOAD_SIZE = new ErrorCode("4008", "您上传的文件大小不能超过[%s]KB");
    public static final ErrorCode SYSTEM_BUSY = new ErrorCode("4009", "系统繁忙，请稍后重试");
    public static final ErrorCode NEED_LOGIN = new ErrorCode("4010", "登录超时,请重新登录");
    public static final ErrorCode OPER_FAIL = new ErrorCode("4011", "操作失败：%s");
    public static final ErrorCode API_RESULT_PARSE_FAIL = new ErrorCode("4012", "API返回值解析失败：%s");
    public static final ErrorCode STATUS_CODE_NOT_SC_OK = new ErrorCode("4013", "API响应Http状态码非200：%s");
    /**
     * 5000通用服务器异常,5001访问redis异常
     */
    public static final ErrorCode SERVER = new ErrorCode("5000", "系统内部错误：%s");
    public static final ErrorCode SERVER_REDIS = new ErrorCode("5001", "系统内部错误：%s");
    public static final ErrorCode SERVER_MYSQL = new ErrorCode("5002", "系统内部错误：%s");
    public static final ErrorCode SERVER_ES = new ErrorCode("5003", "系统内部错误：%s");
    public static final ErrorCode SERVER_HTTP = new ErrorCode("5004", "系统内部错误：%s");
    public static final ErrorCode SERVER_CASSANDRA = new ErrorCode("5005", "系统内部错误：%s");
    public static final ErrorCode SERVER_ORACLE = new ErrorCode("5006", "系统内部错误：%s");

    private String code;
    private String message;
}
