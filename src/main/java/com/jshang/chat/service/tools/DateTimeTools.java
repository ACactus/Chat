package com.jshang.chat.service.tools;

import com.jshang.chat.common.consts.DateConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;

import java.time.LocalDateTime;

/**
 * @Description
 * @Date 2025/12/4 21:08
 * @Author Shawn
 */
@Slf4j
public class DateTimeTools {
    @Tool(description = "获取当前时间")
    String getCurrentDateTime(){
        log.info("时间获取工具被调用");
        return LocalDateTime.now().format(DateConst.DATETIME);
    }
}
