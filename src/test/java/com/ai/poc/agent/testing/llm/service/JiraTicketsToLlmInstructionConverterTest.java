package com.ai.poc.agent.testing.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.ai.poc.agent.llm.service.JiraTicketsToLlmInstructionConverter;
import com.ai.poc.agent.utils.FileUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JiraTicketsToLlmInstructionConverterTest {

    @Test
    void testTransformJiraSearchResponseIssuesToFormattedLlmInstruction() throws Exception {
        String json = FileUtils.readSystemResource("/jira-tickets-data/jiraSearchResponseTickets.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<JiraSearchResponseTicket> jiraIssues = objectMapper.readValue(json, new TypeReference<List<JiraSearchResponseTicket>>() {});

        String prompt = JiraTicketsToLlmInstructionConverter.mapJiraSearchIssuesToFormattedLlmInstruction(jiraIssues);

        // Basic assertions
        Assertions.assertThat(prompt.contains("Review the provided Jira ticket descriptions")).isTrue();
        Assertions.assertThat(prompt).contains("Jira ticket # 1", "Jira ticket # 2", "Jira ticket # 3");

        // verify ticket summaries and descriptions are formatted with tab(s)
        Assertions.assertThat(prompt).contains("\tJira ticket Summary:", "\tJira ticket Description:");
    }

    @Test
    void testTransformJiraCsvFileTicketsToFormattedLlmInstruction() throws Exception {
        String json = FileUtils.readSystemResource("/jira-tickets-data/jiraCsvExportFileTickets.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<JiraTicketDto> jiraTickets = objectMapper.readValue(json, new TypeReference<List<JiraTicketDto>>() {});

        String prompt = JiraTicketsToLlmInstructionConverter.mapJiraExportFileIssuesToFormattedLlmInstruction(jiraTickets);

        // Basic assertions
        Assertions.assertThat(prompt.contains("Review the provided Jira ticket descriptions")).isTrue();
        Assertions.assertThat(prompt).contains("Jira ticket # PROJ-101", "Jira ticket # PROJ-102", "Jira ticket # PROJ-103");

        // verify ticket summaries and descriptions are formatted with tab(s)
        Assertions.assertThat(prompt).contains("\tJira ticket Summary:", "\tJira ticket Description:");
    }
}
