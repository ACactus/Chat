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
                .flatMap(conversation -> this.saveChatHistoryMono(conversation, ChatRoleEnum.USER, request.getUserText()).thenReturn(conversation))
                .flatMapMany(conversation -> {
                    //  先发送会话信息
                    Flux<ServerSentEvent<ChatConversation>> conversationFlux = Flux.just(
                            ServerSentEvent.builder(conversation)
                                    .event(ChatStreamResponseTypeEnum.CONVERSATION_INFO.name())
                                    .build()
                    );

                    // AI 响应流 - 单次订阅，同时收集和流式输出
                    StringBuilder fullResponse = new StringBuilder();
                    Flux<ServerSentEvent<String>> aiResponses = chatFlux(chatClient, request, conversation)
                            .doOnNext(fullResponse::append) // 副作用：收集完整响应
                            .map(text -> ServerSentEvent.builder(text)
                                    .event(ChatStreamResponseTypeEnum.TEXT.name())
                                    .build())
                            .doOnComplete(() -> {
                                // 流结束时保存完整响应
                                saveChatHistoryMono(conversation, ChatRoleEnum.ASSISTANT, fullResponse.toString()).subscribeOn(Schedulers.boundedElastic());
                            });

                    //合并：会话信息 -> AI响应流
                    return Flux.concat(conversationFlux, aiResponses);
                }).onErrorResume(throwable -> {
                    log.error("Chat error", throwable);
                    return Flux.just(ServerSentEvent.builder(throwable.getMessage())
                            .event(ChatStreamResponseTypeEnum.ERROR.name())
                            .build());
                });
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
