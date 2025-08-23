package com.jshang.chat.pojo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ChatRequest
 * @Description
 * @Date 2025/8/10 13:02
 * @Author Shawn
 */
@Data
public class ChatRequest implements Serializable {
    /**
     * 模型
     */
    private String model;

    /**
     * 会话聊天ID
     */
    private String chatSessionId;

    /**
     * 用户输入
     */
    private String userText;
}
