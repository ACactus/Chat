package com.jshang.chat.controller;

import com.jshang.chat.common.consts.ModelConst;
import com.jshang.chat.pojo.dto.chat.ChatRequestDTO;
import com.jshang.chat.pojo.vo.ChatStreamResponseVO;
import com.jshang.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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

    /**
     * 默认使用模型
     */
    private final static String DEFAULT_MODEL = ModelConst.QW_PLUS;

    @PostMapping("/string")
    public Flux<ChatStreamResponseVO<?>> sayHiString(@RequestBody ChatRequestDTO request) {
        if(StringUtils.isBlank(request.getModel())){
            request.setModel(DEFAULT_MODEL);
        }
        return chatService.chatString(request);
    }
}
