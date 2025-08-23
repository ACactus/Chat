package com.jshang.chat.controller;

import com.jshang.chat.pojo.ChatRequest;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

/**
 * @Classname HelloController
 * @Description 聊天输出Demo
 * @Date 2025/7/26 12:55
 * @Author Shawn
 */
@RestController
@RequestMapping("/qw-plus")
@Slf4j
public class HelloController  {
    @Resource
    private Map<String, ChatClient> chatClients;

    /**
     * 默认使用模型
     */
    private final static String DEFAULT_MODEL = "qwTurbo";

    @PostMapping("/string/chat")
    public Flux<String> sayHiString(@RequestBody ChatRequest request) {
        if(StringUtils.isBlank(request.getModel())){
            request.setModel(DEFAULT_MODEL);
        }

        ChatClient chatClient = chatClients.get(request.getModel());
        return chatClient.prompt()
                .user(request.getUserText())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, request.getChatSessionId()))
                .stream()
                .content();
    }
}
