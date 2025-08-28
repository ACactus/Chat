package com.jshang.chat.service;

import com.jshang.chat.common.enums.ChatRoleEnum;
import com.jshang.chat.common.enums.ChatStreamResponseTypeEnum;
import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.dto.chat.ChatRequestDTO;
import com.jshang.chat.pojo.entity.ChatConversation;
import com.jshang.chat.pojo.entity.User;
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

        return Mono.fromCallable(() -> ensureConversation(request))
                .flatMap(conversation -> Mono.fromRunnable(() -> chatHistoryService.saveChatHistory(ChatRoleEnum.USER, conversation.getId(), request.getUserText())).subscribeOn(Schedulers.boundedElastic()).thenReturn(conversation))
                .flatMapMany(conversation -> {
                    //  先发送会话信息
                    ServerSentEvent<ChatConversation> conversationInfo = ServerSentEvent.<ChatConversation>builder()
                            .event(ChatStreamResponseTypeEnum.CONVERSATION_INFO.name())
                            .data(conversation)
                            .build();
                    Flux<ServerSentEvent<ChatConversation>> conversationFlux = Flux.just(conversationInfo);

                    StringBuilder resString = new StringBuilder();
                    //  AI 响应流
                    Flux<String> aiResponses = chatClient.prompt()
                            .user(request.getUserText())
                            .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversation.getSeq()))
                            .stream()
                            .content()
                            .doOnNext(text -> resString.append(text))
                            .concatWith(Mono.fromRunnable(() -> {
                                        chatHistoryService.saveChatHistory(ChatRoleEnum.ASSISTANT, conversation.getId(), resString.toString());
                                    }).subscribeOn(Schedulers.boundedElastic()).thenMany(Flux.empty())
                            );

                    //  把响应流发给前端
                    Flux<ServerSentEvent<String>> textFlux = aiResponses
                            .map(text -> ServerSentEvent.builder(text)
                                    .event(ChatStreamResponseTypeEnum.TEXT.name())
                                    .build());

                    //合并：会话信息 -> AI响应流 -> 结束标记 -> 保存历史
                    return Flux.concat(
                            conversationFlux,
                            textFlux
                    );
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
    private ChatConversation ensureConversation(ChatRequestDTO request) {
        User currUser = ThreadLocalUtil.getUser();
        return StringUtils.isNotBlank(request.getConversationSeq()) ?
                chatConversationService.getConversationBySeq(request.getConversationSeq()) :
                chatConversationService.newConversation(currUser.getId(), generateConversationTitle(request.getUserText()));
    }

    /**
     * 生成会话标题
     *  TODO AI生成
     */
    public String generateConversationTitle(String userText) {
        return userText.substring(0, Math.min(16, userText.length()));
    }
}
