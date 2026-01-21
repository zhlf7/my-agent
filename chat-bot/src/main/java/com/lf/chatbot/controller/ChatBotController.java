package com.lf.chatbot.controller;

import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.cloud.ai.graph.agent.ReactAgent;
import com.alibaba.cloud.ai.graph.checkpoint.savers.MemorySaver;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.lf.chatbot.tool.WeatherTool;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
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
        //zhushi
    }

    @GetMapping("/general-assistant")
    public String chat(@RequestParam(name = "message") String message) {
        // 创建 agent
        ReactAgent agent = ReactAgent.builder()
                .name("智能问答助手")
                .model(chatModel)
                .systemPrompt("你是一个通用助手，归属与Anyi。")
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

    @GetMapping("/weather-assistant")
    public String weather(@RequestParam(name = "message") String message) throws GraphRunnerException {
        ToolCallback weatherTool = FunctionToolCallback.builder("get_weather", new WeatherTool())
                .description("获取给定城市的天气")
                .inputType(String.class)
                .build();

        ReactAgent agent = ReactAgent.builder()
                .name("天气助手")
                .model(chatModel)
                .tools(weatherTool)
                .systemPrompt("你是一个天气助手，只回答天气有关的问题！")
                .saver(new MemorySaver())
                .build();

        AssistantMessage response = agent.call(message);
        return response.getText();
    }

}
