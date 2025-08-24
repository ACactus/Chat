package com.jshang.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jshang.chat.pojo.dto.chathistory.ChatHistoryQueryDTO;
import com.jshang.chat.pojo.entity.ChatHistory;

import java.util.List;

/**
* @author 尚进
* @description 针对表【chat_history(聊天记录表)】的数据库操作Mapper
* @createDate 2025-08-23 22:59:58
* @Entity com.jshang.chat.pojo.entity.ChatHistory
*/
public interface ChatHistoryMapper extends BaseMapper<ChatHistory> {
    List<ChatHistory> listChatHistory(ChatHistoryQueryDTO param);
}
