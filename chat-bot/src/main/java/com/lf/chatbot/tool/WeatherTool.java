package com.lf.chatbot.tool;


import org.springframework.ai.chat.model.ToolContext;

import java.util.function.BiFunction;

/**
 * @author liufeng
 * @date 2025/12/18 11:47
 */
// 定义天气查询工具
public class WeatherTool implements BiFunction<String, ToolContext, String> {
    @Override
    public String apply(String city, ToolContext toolContext) {
        // 可以添加更多模拟逻辑
        String[] weatherConditions = {"晴天", "多云", "雨天", "雪天"};
        String[] temperatures = {"25°C", "18°C", "12°C", "5°C"};

        // 随机选择天气状况（模拟真实变化）
        int randomIndex = (int) (Math.random() * weatherConditions.length);

        return String.format("%s的天气是%s，温度为%s",
                city,
                weatherConditions[randomIndex],
                temperatures[randomIndex]);
    }

}
