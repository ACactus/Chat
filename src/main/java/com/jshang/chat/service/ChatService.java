package com.jshang.chat.service;

import com.jshang.chat.common.enums.ChatRoleEnum;
import com.jshang.chat.common.enums.ChatStreamResponseTypeEnum;
import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.dto.chat.ChatRequestDTO;
import com.jshang.chat.pojo.entity.ChatConversation;
import com.jshang.chat.pojo.entity.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.io.Serializable;
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
     * 响应式流程：查询会话 -> 保存用户输入 -> 流式AI响应 -> 保存AI响应
     */
    public Flux<ServerSentEvent<? extends Serializable>> chatString(ChatRequestDTO request) {
        ChatClient chatClient = chatClients.get(request.getModel());

        return ensureConversation(request)
                .flatMap(conversation -> saveUserMessage(conversation, request.getUserText()))
                .flatMapMany(conversation -> streamChatResponse(chatClient, request, conversation))
                .onErrorResume(this::handleChatError);
    }

    /**
     * 保存用户消息
     */
    private Mono<ChatConversation> saveUserMessage(ChatConversation conversation, String userText) {
        return saveChatHistoryMono(conversation, ChatRoleEnum.USER, userText)
                .thenReturn(conversation);
    }

    /**
     * 流式聊天响应处理
     */
    private Flux<ServerSentEvent<? extends Serializable>> streamChatResponse(
            ChatClient chatClient, ChatRequestDTO request, ChatConversation conversation) {

        // 1. 会话信息事件
        ServerSentEvent<ChatConversation> conversationEvent = ServerSentEvent.builder(conversation)
                .event(ChatStreamResponseTypeEnum.CONVERSATION_INFO.name())
                .build();

        // 2. AI响应流
        return Flux.concat(
                Flux.just(conversationEvent),
                streamAiResponse(chatClient, request, conversation)
        );
    }

    /**
     * 流式AI响应并保存
     */
    private Flux<ServerSentEvent<String>> streamAiResponse(
            ChatClient chatClient, ChatRequestDTO request, ChatConversation conversation) {

        StringBuilder responseCollector = new StringBuilder();

        return chatFlux(chatClient, request, conversation)
                .doOnNext(responseCollector::append)
                .map(text -> ServerSentEvent.builder(text)
                        .event(ChatStreamResponseTypeEnum.TEXT.name())
                        .build())
                .concatWith(saveAiResponse(conversation, responseCollector));
    }

    /**
     * 保存AI响应
     */
    private Mono<ServerSentEvent<String>> saveAiResponse(
            ChatConversation conversation, StringBuilder responseCollector) {

        return Mono.defer(() ->
                saveChatHistoryMono(conversation, ChatRoleEnum.ASSISTANT, responseCollector.toString())
        )
        .map(history -> ServerSentEvent.builder("")
                .event(ChatStreamResponseTypeEnum.SAVE_COMPLETE.name())
                .build())
        .doOnError(error -> log.error("Failed to save AI response", error))
        .onErrorResume(error -> Mono.empty());
    }

    /**
     * 处理聊天错误
     */
    private Flux<ServerSentEvent<? extends Serializable>> handleChatError(Throwable throwable) {
        log.error("Chat error", throwable);
        return Flux.just(ServerSentEvent.builder(throwable.getMessage())
                .event(ChatStreamResponseTypeEnum.ERROR.name())
                .build());
    }

    /**
     * 会话存在则返回会话内容，否则新建一个会话
     */
    private Mono<ChatConversation> ensureConversation(ChatRequestDTO request) {
        Long userId = ThreadLocalUtil.getUserId();
        String conversationSeq = request.getConversationSeq();
        String userText = request.getUserText();
        return Mono.fromCallable(() -> StringUtils.isNotBlank(conversationSeq) ? chatConversationService.getConversationBySeq(conversationSeq) : chatConversationService.newConversation(userId, generateConversationTitle(userText)))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * 保存聊天记录
     */
    private Mono<ChatHistory> saveChatHistoryMono(ChatConversation conversation, ChatRoleEnum roleEnum, String content) {
        return Mono.fromCallable(() -> chatHistoryService.saveChatHistory(roleEnum, conversation.getId(), content)).subscribeOn(Schedulers.boundedElastic());
    }


    /**
     * AI响应
     */
    private Flux<String> chatFlux(ChatClient chatClient, ChatRequestDTO request, ChatConversation conversation) {
        //  AI 响应流
        return chatClient.prompt()
                .user(request.getUserText())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversation.getSeq()))
                .stream()
                .content();
    }

    /**
     * 生成会话标题
     *  TODO AI生成
     */
    public String generateConversationTitle(String userText) {
        return userText.substring(0, Math.min(16, userText.length()));
    }
}
