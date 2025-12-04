package com.lf.chatbot.controller;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class OllamaController {

    private static final String DEFAULT_PROMPT = "你是一个博学的智能聊天助手，请根据用户提问回答！";

    @Resource(name = "ollamaChatModel")
    private ChatModel chatModel;

    @GetMapping("/simple/chat")
    public String simpleChat(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        try {
            // 创建 agent
            ReactAgent agent = ReactAgent.builder()
                    .name("weather_agent")
                    .model(chatModel)
                    .systemPrompt(DEFAULT_PROMPT)
                    .saver(new MemorySaver())
                    .build();

            AssistantMessage response = agent.call(query);
            return response.getText();
        } catch (GraphRunnerException e) {
            // 打印日志或做其他异常处理
            e.printStackTrace();
            return "抱歉，处理请求时发生错误，请稍后再试。";
        }
    }

    @GetMapping("/simple/chat1")
    public Flux<String> simpleChat1(@RequestParam(value = "query", defaultValue = "你好，很高兴认识你，能简单介绍一下自己吗？") String query) {
        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .systemPrompt(DEFAULT_PROMPT)
                .saver(new MemorySaver())
                .build();

        Flux<NodeOutput> stream = null;
        try {
            stream = agent.stream(query);
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
     return null;
    }


}
