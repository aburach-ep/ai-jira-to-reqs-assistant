package com.ai.poc.agent.dial.service;

import com.ai.poc.agent.dial.dto.DialRequestDto;
import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DialRequestBuilder {

    public static final String ROLE_USER = "user";

    private static final String AI_REQUEST_TEMPLATE = "ai-prompts/ticket-review-instructions.txt";
    private static final String JIRA_TICKET_TITLE = "jiraTicketTitle";
    private static final String JIRA_TICKET_DESCRIPTION = "jiraTicketDescription";

    public DialRequestDto mapJiraResponseIssueToDialRequest(JiraSearchResponseTicket jiraIssue) {
        var templateRequest = FileUtils.readSystemResource(AI_REQUEST_TEMPLATE);
        Map<String, String> templateVariables = Map.ofEntries(
                Map.entry(JIRA_TICKET_TITLE, jiraIssue.fields.summary),
                Map.entry(JIRA_TICKET_DESCRIPTION, jiraIssue.fields.description)
        );
        StringSubstitutor substitutor = new StringSubstitutor(templateVariables);
        var ticketReviewRequest = substitutor.replace(templateRequest);
        return createDialRequest(ticketReviewRequest);
    }

    private static DialRequestDto createDialRequest(String llmRequest) {
        return DialRequestDto.builder()
                .messages(
                    List.of(
                        DialRequestDto.DialRequestMessage
                            .builder()
                            .role(ROLE_USER)
                            .content(llmRequest)
                            .build()
                    )
                )
                .build();
    }

}
