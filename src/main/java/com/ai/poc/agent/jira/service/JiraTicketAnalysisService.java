package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.dial.client.LlmClient;
import com.ai.poc.agent.jira.client.JiraUpdateService;
import com.ai.poc.agent.dial.dto.DialResponseDto;
import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.jira.dto.JiraTicketAnalysisResultDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.LF;

@Service
@RequiredArgsConstructor
public class JiraTicketAnalysisService {

    private static final String DIAL_API_VERSION = "2024-12-01-preview";

    private final JiraSearchService jiraSearchService;
    private final DialRequestBuilder dialRequestBuilder;
    private final LlmClient dialClient;
    private final JiraUpdateService jiraUpdateService;

    @Value("${dial.api-key}")
    private String dialApiKey;

    @Value("${dial.foundation-model}")
    private String dialModel;

    public List<JiraTicketAnalysisResultDto> analyzeTickets(String projectName, String searchText) {
        JiraSearchResponse searchResponse = jiraSearchService.findSingleTicketBySummary(projectName, searchText);

        List<JiraTicketAnalysisResultDto> ticketResults = new ArrayList<>();
        for (JiraSearchResponseIssue issue : searchResponse.getIssues()) {
            var analyzeTicketRequest = dialRequestBuilder.mapJiraResponseIssueToDialRequest(issue);
            DialResponseDto dialResponseDto = dialClient.callChatApi(dialApiKey, DIAL_API_VERSION, analyzeTicketRequest);

            for (DialResponseDto.Choice dialResponseChoice : dialResponseDto.choices) {
                String dialResponseContent = dialResponseChoice.message.content;
                printIssueDetailsAndDialResponse(issue, dialResponseContent);
                JiraTicketAnalysisResultDto ticketAnalysisResult = parseDialResponse(issue.key, dialResponseContent);

                // Update Jira ticket with potential questions using MCP client
                if (StringUtils.isNotBlank(ticketAnalysisResult.getPotentialQuestions())) {
                    jiraUpdateService.addCommentToTicket(issue.key,
                        "AI Analysis - Potential Questions:\n" + ticketAnalysisResult.getPotentialQuestions());
                }

                ticketResults.add(ticketAnalysisResult);
            }
        }
        return ticketResults;
    }

    private static void printIssueDetailsAndDialResponse(JiraSearchResponseIssue issue, String dialResponseContent) {
        System.out.println("<--- Original issue Summary: " + issue.fields.summary + " --->\n");
        System.out.println("<--- Original issue Description: " + issue.fields.description + " --->\n");
        System.out.println("<--- DIAL response Message: " + LF + dialResponseContent + LF + " --->\n");
    }

    private static JiraTicketAnalysisResultDto parseDialResponse(String jiraTicketKey, String dialResponse) {
        JiraTicketAnalysisResultDto dto = new JiraTicketAnalysisResultDto();
        dto.setTicketKey(jiraTicketKey);

        // Use regex to extract sections
        String completeness = StringUtils.substringBetween(dialResponse, "1. ", "2. ");
        String summary = StringUtils.substringBetween(dialResponse, "2. ", "3. ");
        String questions = StringUtils.substringAfter(dialResponse, "3. ");

        dto.setCompletenessEvaluation(StringUtils.trim(completeness));
        dto.setTicketSummary(StringUtils.trim(summary));
        dto.setPotentialQuestions(StringUtils.trim(questions));

        return dto;
    }
}
