package com.jshang.chat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname ChatModelConfig
 * @Description TODO
 * @Date 2025/7/26 16:03
 * @Author Shawn
 */
@Configuration
public class ChatModelConfig {
    @Bean(name = "qwPlus")
    public ChatModel qwPlus(OpenAiChatModel baseModel) {
        return baseModel.mutate()
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("qwen-plus")
                        .build())
                .build();
    }
}
