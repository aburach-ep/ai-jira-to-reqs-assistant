package com.ai.poc.agent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@Slf4j
@EnableFeignClients
@EnableConfigurationProperties
public class AiJiraToRequirementsAssistantApplication {

    public static void main(String[] args) {
        // run the microservice in background
        SpringApplication.run(AiJiraToRequirementsAssistantApplication.class, args);
    }

}