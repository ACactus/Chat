package com.jshang.chat.common.exception;

import com.jshang.chat.common.enums.ServiceCodeEnum;
import lombok.Data;

/**
 * @Classname BusinessException
 * @Description 基础业务异常
 * @Date 2025/8/24 10:16
 * @Author Shawn
 */
@Data
public class BusinessException extends RuntimeException {
    /**
     * 错误码
     */
    private final Integer code;

    /**
     * 错误信息
     */
    private final String message;

    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BusinessException(ServiceCodeEnum serviceCodeEnum){
        this(serviceCodeEnum.getCode(), serviceCodeEnum.getDescription());
    }
}
