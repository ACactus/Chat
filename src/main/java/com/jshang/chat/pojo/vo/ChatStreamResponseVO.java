package com.jshang.chat.pojo.vo;

import com.jshang.chat.common.enums.ChatStreamResponseTypeEnum;
import com.jshang.chat.pojo.entity.ChatConversation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Classname ChatStreamResponseVO
 * @Description 聊天流式响应VO
 * @Date 2025/8/24 9:56
 * @Author Shawn
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class ChatStreamResponseVO<T> {
    /**
     * 指令类型
     * @see com.jshang.chat.common.enums.ChatStreamResponseTypeEnum
     */
    private Integer type;

    /**
     * 内容
     */
    private T data;

    /**
     * 是否完成
     */
    private Boolean completed;

    /**
     * 构造一个包含会话信息的流式响应
     */
    public static ChatStreamResponseVO<ChatConversation> conversationInfo(ChatConversation  conversation, boolean completed){
        return new ChatStreamResponseVO<>(ChatStreamResponseTypeEnum.CONVERSATION_INFO.getCode(), conversation, completed);
    }

    /**
     * 构造一个包含字符串的流式响应
     */
    public static ChatStreamResponseVO<String> chatString(String text){
        return new ChatStreamResponseVO<>(ChatStreamResponseTypeEnum.CHAT_STRING.getCode(), text, false);
    }

    /**
     * 构造一个结束的流式响应
     */
    public static ChatStreamResponseVO<String> end(){
        return new ChatStreamResponseVO<>(ChatStreamResponseTypeEnum.CHAT_STRING.getCode(), null, true);
    }
}
