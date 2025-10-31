package com.ai.poc.agent.testing.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.llm.service.SapLlmRequestBuilder;
import com.ai.poc.agent.utils.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class SapLlmRequestBuilderTest {

    @Test
    public void testTransformJiraIssuesToFormattedLlmInstruction() throws Exception {
        String json = FileUtils.readSystemResource("/jira-responses/jiraSearchResponseIssues.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<JiraSearchResponseIssue> jiraIssues = objectMapper.readValue(
                json, new TypeReference<List<JiraSearchResponseIssue>>() {});

        String prompt = SapLlmRequestBuilder.transformJiraIssuesToFormattedLlmInstruction(jiraIssues);

        // Basic assertions
        Assertions.assertThat(prompt.contains("Review the provided Jira ticket descriptions")).isTrue();
        Assertions.assertThat(prompt).contains("Jira ticket # 1", "Jira ticket # 2", "Jira ticket # 3");

        // verify ticket summaries and descriptions are formatted with tab(s)
        Assertions.assertThat(prompt).contains("\tJira ticket Summary:", "\tJira ticket Description:");
    }
}
