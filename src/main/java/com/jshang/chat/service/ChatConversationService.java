package com.jshang.chat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jshang.chat.mapper.ChatConversationMapper;
import com.jshang.chat.pojo.entity.ChatConversation;
import com.jshang.chat.pojo.vo.ChatConversationListItemVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
* @author Shawn
* @description 针对表【chat_conversation(聊天会话表)】的数据库操作Service实现
* @createDate 2025-08-23 22:59:58
*/
@Service
public class ChatConversationService extends ServiceImpl<ChatConversationMapper, ChatConversation>{
    /**
     * 获取用户的会话列表
     */
    public List<ChatConversationListItemVO> listConversationVO(Long userId){
        return lambdaQuery()
                .eq(ChatConversation::getUserId, userId)
                .list()
                .stream()
                .map(ChatConversationListItemVO::ofEntity)
                .toList();
    }

    /**
     * 根据seq获取会话
     */
    public ChatConversation getConversationBySeq(String seq){
        return lambdaQuery().eq(ChatConversation::getSeq, seq).one();
    }

    /**
     * 新创建一个会话
     */
    public ChatConversation newConversation(String seq, Long userId, String title) {
        // 新建会话
        ChatConversation conversation = new ChatConversation();
        String newConversationSeq = StringUtils.isNotBlank(seq) ? seq : UUID.randomUUID().toString().replace("-", "");
        conversation.setSeq(newConversationSeq);
        conversation.setTitle(title);
        conversation.setUserId(userId);
        save(conversation);
        return conversation;
    }
}




