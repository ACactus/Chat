package com.jshang.chat.pojo.dto.chat;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jshang.chat.common.consts.ModelConst;
import lombok.Data;

import java.io.Serializable;

/**
 * @Classname ChatRequest
 * @Description
 * @Date 2025/8/10 13:02
 * @Author Shawn
 */
@Data
public class ChatRequestDTO implements Serializable {
    /**
     * 模型
     * @see ModelConst
     */
    private String model;

    /**
     * 会话聊天ID
     */
    private String conversationSeq;

    /**
     * 用户输入
     */
    private String userText;

    /**
     * 请求用户Id，Web接口赋值
     */
    @JsonIgnore
    private Long userId;
}
