package com.jshang.chat.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
/**
 * @Classname ChatModelConfig
 * @Description 配置聊天客户端
 * @Date 2025/7/26 16:03
 * @Author Shawn
 */
@Configuration
public class ChatClientConfig {

    /**
     * 默认客户端：qw-turbo
     */
    @Bean
    public ChatClient openAiChatClient(OpenAiChatModel chatModel) {
        return ChatClient.create(chatModel);
    }

    /**
     * qw-plus
     */
    @Bean("qwPlusClient")
    public ChatClient qwPlusClient(ChatModel qwPlus) {
        return ChatClient.create(qwPlus);
    }
}
