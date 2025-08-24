package com.jshang.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum ServiceCodeEnum {

    /**
     * 成功
     */
    OK(200, "请求成功"),

    /**
     * 请求错误
     */
    BAD_REQUEST(400, "错误请求"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源未找到"),
    METHOD_NOT_ALLOWED(405, "方法不允许"),
    REQUEST_TIMEOUT(408, "请求超时"),
    CONFLICT(409, "冲突"),
    GONE(410, "已删除"),
    UNSUPPORTED_MEDIA_TYPE(415, "不支持的媒体类型"),
    TOO_MANY_REQUESTS(429, "请求过多"),

    /**
     * 服务器错误
     */
    INTERNAL_SERVER_ERROR(500, "服务器内部错误"),
    NOT_IMPLEMENTED(501, "未实现"),
    BAD_GATEWAY(502, "错误网关"),
    SERVICE_UNAVAILABLE(503, "服务不可用"),
    GATEWAY_TIMEOUT(504, "网关超时");

    private final Integer code;
    private final String description;

    public static ServiceCodeEnum ofCode(Integer code){
        return Arrays.stream(ServiceCodeEnum.values())
                .filter(serviceCodeEnum -> serviceCodeEnum.code.equals(code))
                .findFirst()
                .orElse(null);
    }
}