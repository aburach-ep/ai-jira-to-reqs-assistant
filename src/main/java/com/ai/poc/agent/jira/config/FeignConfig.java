package com.ai.poc.agent.jira.config;

import feign.Logger;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {

    @Bean
    Logger.Level feignLoggerLevel() {
        // print URL, headers and message body when calling an external service via Feign
        return Logger.Level.FULL;
    }
} 