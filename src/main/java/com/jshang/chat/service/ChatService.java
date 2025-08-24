package com.jshang.chat.service;

import com.jshang.chat.common.enums.ChatRoleEnum;
import com.jshang.chat.common.enums.ChatStreamResponseTypeEnum;
import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.dto.chat.ChatRequestDTO;
import com.jshang.chat.pojo.entity.ChatConversation;
import com.jshang.chat.pojo.entity.ChatHistory;
import com.jshang.chat.pojo.entity.User;
import com.jshang.chat.pojo.vo.ChatStreamResponseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Map;

/**
 * @Classname ChatService
 * @Description ChatClient业务逻辑
 * @Date 2025/8/24 13:21
 * @Author Shawn
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatService {
    private final Map<String, ChatClient> chatClients;

    private final ChatHistoryService chatHistoryService;

    private final ChatConversationService chatConversationService;

    /**
     * 文本聊天业务逻辑
     * 输出顺序：会话信息 -> 聊天内容 -> 结束
     */
    public Flux<ChatStreamResponseVO<?>> chatString(ChatRequestDTO request) {
        ChatClient chatClient = chatClients.get(request.getModel());
        // 存储完整的LLM响应
        StringBuffer llmResponse = new StringBuffer();
        User currUser = ThreadLocalUtil.getUser();
        // 查询会话信息，无会话流水号则新建一个会话
        String userText = request.getUserText();
        ChatConversation conversation = StringUtils.isNotBlank(request.getConversationSeq()) ?
                chatConversationService.getConversationBySeq(request.getConversationSeq()) : chatConversationService.newConversation(currUser.getId(), generateConversationTitle(userText));
        ChatHistory history = chatHistoryService.saveChatHistory(ChatRoleEnum.USER, conversation.getId(), request.getUserText());

        // 会话信息
        Flux<ChatStreamResponseVO<ChatConversation>> conversationInfo = Flux.just(new ChatStreamResponseVO<>(ChatStreamResponseTypeEnum.CONVERSATION_INFO.getCode(), conversation, false));

        // 流式聊天内容
        Flux<ChatStreamResponseVO<String>> chatContent = chatClient.prompt()
                .user(userText)
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversation.getSeq()))
                .stream()
                .content()
                .map(ChatStreamResponseVO::chatString)
                .doOnNext(content -> llmResponse.append(content.getData()))
                .doOnComplete(() -> chatHistoryService.saveChatHistory(ChatRoleEnum.ASSISTANT, history.getConversationId(), llmResponse.toString()));
        // 结束标识
        Flux<ChatStreamResponseVO<String>> endFlag = Flux.just(ChatStreamResponseVO.end());
        // 合并会话信息和聊天内容
        return Flux.concat(conversationInfo, chatContent, endFlag);
    }

    /**
     * 生成会话标题
     *  TODO AI生成
     */
    public String generateConversationTitle(String userText){
        return userText.substring(0, Math.min(16, userText.length()));
    }
}
