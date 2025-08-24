package com.jshang.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname ChatRateEnum
 * @Description 聊天评价枚举
 * @Date 2025/8/24 16:14
 * @Author Shawn
 */
@AllArgsConstructor
@Getter
public enum ChatRateEnum {
    /**
     * 未评价
     */
    UNRATED(0),

    /**
     * 好评
     */
    GOOD(1),

    /**
     * 差评
     */
    BAD(2),
    ;
    private final Integer code;

    public static ChatRateEnum ofCode(Integer code) {
        for (ChatRateEnum value : values()) {
            if (value.code.equals(code)) {
                return value;
            }
        }
        return null;
    }
}
