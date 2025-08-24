package com.jshang.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname ChatRoleEnum
 * @Description 聊天角色枚举
 * @Date 2025/8/24 13:30
 * @Author Shawn
 */
@AllArgsConstructor
@Getter
public enum ChatRoleEnum {
    USER(1, "用户"),
    ASSISTANT(2, "助手"),
    SYSTEM(3, "系统"),
    ;
    /**
     * 枚举值
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;

}
