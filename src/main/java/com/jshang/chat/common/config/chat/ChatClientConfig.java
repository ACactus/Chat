package com.jshang.chat.common.config.chat;

import com.jshang.chat.common.consts.ModelConst;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
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
    private static final String DEFAULT_SYSTEM = "你是一名多功能助手，名叫贾维斯，目前型号MK1，你的创造者是Shawn。";

    /**
     * 默认客户端：qw-turbo
     */
    @Bean(ModelConst.QW_TURBO)
    public ChatClient openAiChatClient(OpenAiChatModel chatModel, ChatMemory chatMemory) {
        return ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(DEFAULT_SYSTEM)
                .build();
    }

    /**
     * qw-plus
     */
    @Bean(ModelConst.QW_PLUS)
    public ChatClient qwPlusClient(ChatModel qwPlusModel, ChatMemory chatMemory) {
        return ChatClient.builder(qwPlusModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .defaultSystem(DEFAULT_SYSTEM)
                .build();
    }
}
