package com.lf.chatbot.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author liufeng
 * @date 2025/12/4 13:40
 */
@RestController
public class ChatBotController {


    private final ChatModel chatModel;

    public ChatBotController() {
        String apiKey = System.getenv("AI_DASHSCOPE_API_KEY");
        DashScopeApi dashScopeApi = DashScopeApi.builder()
                .apiKey(apiKey)
                .build();
        chatModel = DashScopeChatModel.builder()
                .dashScopeApi(dashScopeApi)
                .build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam(name = "message") String message) {
        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("weather_agent")
                .model(chatModel)
                .systemPrompt("You are a helpful assistant")
                .saver(new MemorySaver())
                .build();

        AssistantMessage response;
        try {
            response = agent.call(message);
        } catch (GraphRunnerException e) {
            throw new RuntimeException(e);
        }
        return response.getText();
    }

}
