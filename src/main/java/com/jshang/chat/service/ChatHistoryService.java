package com.jshang.chat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jshang.chat.common.enums.ChatRateEnum;
import com.jshang.chat.common.enums.ChatRoleEnum;
import com.jshang.chat.mapper.ChatHistoryMapper;
import com.jshang.chat.pojo.dto.chathistory.ChatHistoryQueryDTO;
import com.jshang.chat.pojo.entity.ChatHistory;
import com.jshang.chat.pojo.vo.ChatHistoryListItemVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
* @author Shawn
* @description 针对表【chat_history(聊天记录表)】的数据库操作Service实现
* @createDate 2025-08-23 22:59:58
*/
@Service
@RequiredArgsConstructor
public class ChatHistoryService extends ServiceImpl<ChatHistoryMapper, ChatHistory>{
    private final ChatHistoryMapper chatHistoryMapper;

    private final ChatConversationService chatConversationService;

    /**
     * 条件查询聊天记录列表
     */
    public List<ChatHistoryListItemVO> listChatHistory(ChatHistoryQueryDTO conversationId) {
        List<ChatHistory> historyList = chatHistoryMapper.listChatHistory(conversationId);
        return historyList.stream()
                .map(ChatHistoryListItemVO::ofEntity)
                .toList();
    }

    /**
     * 保存AI助手聊天记录
     */
    public ChatHistory saveChatHistory(ChatRoleEnum roleEnum,  Long conversationId, String content) {
        ChatHistory history = new ChatHistory();
        history.setRole(roleEnum.getCode());
        history.setSeq(UUID.randomUUID().toString().replace("-", ""));
        history.setRawContent(content);
        history.setConversationId(conversationId);
        history.setRate(ChatRateEnum.UNRATED.getCode());
        super.save(history);
        return history;
    }
}




