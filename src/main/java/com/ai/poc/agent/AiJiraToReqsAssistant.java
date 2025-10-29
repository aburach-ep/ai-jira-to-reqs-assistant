package com.ai.poc.agent;

import com.ai.poc.agent.jira.service.JiraTicketAnalysisService;
import com.ai.poc.agent.service.JiraToRequirementsTransformerService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties
public class AiJiraToReqsAssistant {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(AiJiraToReqsAssistant.class, args);
        var jiraTjiraToRequirementsTransformerService = applicationContext.getBean(JiraToRequirementsTransformerService.class);

        jiraTjiraToRequirementsTransformerService.findAndTransformJiraTicketsToRequirements();

        applicationContext.stop();
    }

}