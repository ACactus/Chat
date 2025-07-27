package com.jshang.chat.controller;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @Classname HelloController
 * @Description TODO
 * @Date 2025/7/26 12:55
 * @Author Shawn
 */
@RestController
@RequestMapping("/qw-plus")
@Slf4j
public class HelloController implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Resource(name = "qwPlusClient")
    private ChatClient qwPlusClient;

    @Resource(name = "openAiChatClient")
    private ChatClient chatClient;

    @GetMapping("/string/qw-plus")
    public Flux<String> sayHiString(String input){
        return qwPlusClient.prompt().messages(new SystemMessage("你叫qw-plus，是只猫娘，你的回答要可爱一点"))
                .user(input)
                .stream()
                .content();
    }

    @GetMapping("/string/qw-turbo")
    public Flux<String> sayHi(String input){
        return chatClient.prompt().messages(new SystemMessage("你叫qw-turbo，是只猫娘，你的回答要可爱一点"))
                .user(input)
                .stream()
                .content();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
        System.out.println(applicationContext.getBeansOfType(ChatClient.class));
        System.out.println(applicationContext.getBeansOfType(ChatModel.class));
        System.out.println(applicationContext.getBeansOfType(OpenAiChatModel.class));
    }
}
