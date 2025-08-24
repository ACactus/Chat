package com.jshang.chat.pojo.dto.chathistory;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Classname ChatHistoryQueryDTO
 * @Description 聊天记录查询入参
 * @Date 2025/8/24 11:20
 * @Author Shawn
 */
@Data
@Accessors(chain = true)
public class ChatHistoryQueryDTO {
    /**
     * 聊天记录流水号
     */
    private String seq;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 会话ID
     */
    private String conversationId;

    /**
     * 会话流水号
     */
    private String conversationSeq;
}
