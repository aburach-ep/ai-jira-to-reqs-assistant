package com.ai.poc.agent;

import com.ai.poc.agent.service.JiraToRequirementsTransformerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
public class AiJiraToReqsAssistantApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AiJiraToReqsAssistantApplication.class, args);
        var jiraTjiraToRequirementsTransformerService = applicationContext.getBean(JiraToRequirementsTransformerService.class);

        jiraTjiraToRequirementsTransformerService.findAndTransformJiraTicketsToRequirements("EPMCDMETST-14052");

    }

}