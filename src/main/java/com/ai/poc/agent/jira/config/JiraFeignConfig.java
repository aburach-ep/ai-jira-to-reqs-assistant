package com.ai.poc.agent.jira.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JiraFeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // print URL, headers and message body when calling an external service via Feign
        return Logger.Level.FULL;
    }
} 