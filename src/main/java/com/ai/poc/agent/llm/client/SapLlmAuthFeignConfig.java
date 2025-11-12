package com.ai.poc.agent.llm.client;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SapLlmAuthFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // print URL, headers and message body when calling an external service via Feign
        return Logger.Level.NONE;
    }
} 