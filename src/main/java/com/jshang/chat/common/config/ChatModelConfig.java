package com.jshang.chat.common.config;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Classname ChatModelConfig
 * @Description 配置不同模型
 * @Date 2025/7/26 16:03
 * @Author Shawn
 */
@Configuration
public class ChatModelConfig {
    @Bean(name = "qwPlusModel")
    public ChatModel qwPlus(OpenAiChatModel baseModel) {
        return baseModel.mutate()
                .defaultOptions(OpenAiChatOptions.builder()
                        .model("qwen-plus")
                        .build())
                .build();
    }
}
