package com.jshang.chat.controller;

import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.RestResponse;
import com.jshang.chat.pojo.dto.chathistory.ChatHistoryQueryDTO;
import com.jshang.chat.pojo.vo.ChatConversationListItemVO;
import com.jshang.chat.pojo.vo.ChatHistoryListItemVO;
import com.jshang.chat.service.ChatConversationService;
import com.jshang.chat.service.ChatHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Classname ChatHistoryController
 * @Description 聊天历史web接口
 * @Date 2025/8/24 10:43
 * @Author Shawn
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ChatHistoryController {
    private final ChatHistoryService chatHistoryService;

    private final ChatConversationService chatConversationService;

    /**
     * userId查询聊天会话列表
     */
    @GetMapping("/conversation/list")
    public RestResponse<List<ChatConversationListItemVO>> listConversation(){
        Long userId = ThreadLocalUtil.getUserId();
        return RestResponse.ok(chatConversationService.listConversationVO(userId));
    }

    /**
     * 查询指定会话聊天记录
     */
    @PostMapping("/history/list")
    public RestResponse<List<ChatHistoryListItemVO>> listChatHistory(@RequestBody ChatHistoryQueryDTO param){
        return RestResponse.ok(chatHistoryService.listChatHistory(param));
    }
}
