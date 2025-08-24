package com.jshang.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname ChatModelEnum
 * @Description 可用大模型枚举
 * @Date 2025/8/24 12:52
 * @Author Shawn
 */
@AllArgsConstructor
@Getter
public enum ChatModelEnum {
    QW_TURBO("qwTurbo"),
    QW_PLUS("qwPlus"),
    ;
    private final String model;

    public static void main(String[] args) {
    }

}
