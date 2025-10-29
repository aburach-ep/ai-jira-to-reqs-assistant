package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.jira.client.JiraClient;
import com.ai.poc.agent.jira.dto.JiraSearchRequestDto;
import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JiraSearchService {

    public static final String JIRA_SEARCH_REQ_BY_SUMMARY = "project = \"%s\" AND text ~ \"%s\"";
    public static final String JIRA_SEARCH_REQ_PART_MULTIPLE_TICKETS = "project = \"%s\" AND type = \"Task\"";
    private static final int SINGLE_TICKET_SEARCH_RESULT_SIZE = 1;
    private static final int MULTI_TICKETS_SEARCH_RESULT_SIZE = 3;

    private final JiraClient jiraClient;

    public JiraSearchResponse findSingleTicketBySummary(String projectName, String searchText) {
        var jiraSearchRequest = createJiraSearchRequestBySummary(projectName, searchText);
        JiraSearchResponse searchResponse = jiraClient.findItems(jiraSearchRequest);
        return searchResponse;
    }

    public JiraSearchResponse findMultipleTicketsByKeywords(String projectName, List<String> searchKeywords) {
        var jiraSearchRequest = createJiraSearchRequestByKeywords(projectName, searchKeywords);
        JiraSearchResponse searchResponse = jiraClient.findItems(jiraSearchRequest);
        return searchResponse;
    }

    private static JiraSearchRequestDto createJiraSearchRequestBySummary(String projectName, String jiraSearchSummary) {
        var jql = String.format(JIRA_SEARCH_REQ_BY_SUMMARY, projectName, jiraSearchSummary);
        var jiraSearchRequest = JiraSearchRequestDto.builder()
                .jql(jql)
                .maxResults(SINGLE_TICKET_SEARCH_RESULT_SIZE)
                .build();
        return jiraSearchRequest;
    }

    private static JiraSearchRequestDto createJiraSearchRequestByKeywords(String projectName, List<String> jiraSearchKeywords) {
        var jqlQueryPartTextLikeDelimitedByOr = jiraSearchKeywords.stream()
                .map(el -> String.format(" text ~ \"%s\"", el))
                .collect(Collectors.joining(" OR "));
        var jqlPrefix = String.format(JIRA_SEARCH_REQ_PART_MULTIPLE_TICKETS, projectName);
        var targetJql = jqlPrefix;
        if (CollectionUtils.isNotEmpty(jiraSearchKeywords)) {
            targetJql = StringUtils.join(jqlPrefix, " AND ", jqlQueryPartTextLikeDelimitedByOr);
        }
        var jiraSearchRequest = JiraSearchRequestDto.builder()
                .jql(targetJql)
                .maxResults(MULTI_TICKETS_SEARCH_RESULT_SIZE)
                .build();
        return jiraSearchRequest;
    }

}
