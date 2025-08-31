package com.jshang.chat.common.config.web;

import com.jshang.chat.common.exception.BusinessException;
import com.jshang.chat.pojo.RestResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Description 全局异常处理
 * @Date 2025/8/31 15:08
 * @Author Shawn
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * 自定义业务异常处理
     */
    @ExceptionHandler(BusinessException.class)
    public RestResponse<Void> handlerBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage());
        return RestResponse.fail(e.getCode(), e.getMessage());
    }
}
