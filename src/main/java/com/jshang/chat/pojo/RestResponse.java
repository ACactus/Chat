package com.jshang.chat.pojo;

import com.jshang.chat.common.enums.ServiceCodeEnum;
import com.jshang.chat.common.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname RestResponse
 * @Description 响应数据封装
 * @Date 2025/8/24 9:56
 * @Author Shawn
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class RestResponse<T> {
    private Integer code;

    private String message;

    private T data;

    public static <T> RestResponse<T> ok(T data) {
        return ok(ServiceCodeEnum.OK.getDescription(), data);
    }

    public static <T> RestResponse<T> ok(String message, T data) {
        return new RestResponse<T>().setCode(ServiceCodeEnum.OK.getCode()).setMessage(message).setData(data);
    }

    public static <T> RestResponse<T> fail(Integer code, String message) {
        return new RestResponse<T>().setCode(code).setMessage(message);
    }

    public static <T> RestResponse<T> fail(BusinessException ex) {
        return fail(ex.getCode(), ex.getMessage());
    }

    public static <T> RestResponse<T> fail(ServiceCodeEnum serviceCodeEnum) {
        return fail(serviceCodeEnum.getCode(), serviceCodeEnum.getDescription());
    }

}
