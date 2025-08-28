package com.jshang.chat.controller;

import com.jshang.chat.common.consts.ModelConst;
import com.jshang.chat.common.utils.ThreadLocalUtil;
import com.jshang.chat.pojo.dto.chat.ChatRequestDTO;
import com.jshang.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.io.Serializable;
import java.util.Map;

/**
 * @Description 聊天输出Demo
 * @Date 2025/7/26 12:55
 * @Author Shawn
 */
@RestController
@RequestMapping("/chat")
@Slf4j
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;
    private final Map<String, ChatClient> chatClients;
    /**
     * 默认使用模型
     */
    private static final String DEFAULT_MODEL = ModelConst.QW_PLUS;

    @PostMapping(value = "/string", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<? extends Serializable>> chatString(@RequestBody ChatRequestDTO request) {
        if(StringUtils.isBlank(request.getModel())){
            request.setModel(DEFAULT_MODEL);
        }
        request.setUserId(ThreadLocalUtil.getUser().getId());
        return chatService.chatString(request);
    }

    @GetMapping(value = "/test", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<? extends Serializable>> test(ChatRequestDTO request) {
        if(StringUtils.isBlank(request.getModel())){
            request.setModel(DEFAULT_MODEL);
        }
        return chatService.chatString(request);
    }
}
