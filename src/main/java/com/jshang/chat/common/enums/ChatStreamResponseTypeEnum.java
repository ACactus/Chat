package com.jshang.chat.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Classname ChatStreamResponseTypeEnum
 * @Description 聊天流式响应类型枚举
 * @Date 2025/8/24 19:04
 * @Author Shawn
 */
@AllArgsConstructor
@Getter
public enum ChatStreamResponseTypeEnum {

    /**
     * 会话信息
     */
    CONVERSATION_INFO,

    /**
     * 文本开始
     */
    TEXT,

    /**
     * 错误信息
     */
    ERROR,

    /**
     * 保存完成
     */
    SAVE_COMPLETE,
    ;
}
