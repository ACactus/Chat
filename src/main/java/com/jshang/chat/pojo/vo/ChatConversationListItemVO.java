package com.jshang.chat.pojo.vo;

import com.jshang.chat.pojo.entity.ChatConversation;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

/**
 * @Description 聊天会话列表项VO
 * @Date 2025/8/24 10:46
 * @Author Shawn
 */
@Data
public class ChatConversationListItemVO {
    /**
     * 流水号
     */
    private String seq;

    /**
     * 标题
     */
    private String title;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    public static ChatConversationListItemVO ofEntity(ChatConversation entity) {
        ChatConversationListItemVO vo = new ChatConversationListItemVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
