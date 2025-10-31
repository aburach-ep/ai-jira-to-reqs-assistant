package com.ai.poc.agent.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.jira.service.JiraSearchService;
import com.ai.poc.agent.llm.client.SapLlmClient;
import com.ai.poc.agent.llm.service.SapLlmApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraToRequirementsTransformerService {

    private final JiraSearchService jiraSearchService;
    private final SapLlmApiService llmApiService;

    // note the inscription is expected to be in bold in Jira, otherwise search won't find the phrase
    private static final String RELATED_TICKETS_PREFIX = "{*}Related Tickets{*}: ";

    public void findAndTransformJiraTicketsToRequirements() {
        var singleTicketSearchProjectName = "EPMCDMETST";
        var singleTicketSearchResults = retrieveProvidedTicketDescription(singleTicketSearchProjectName, "experimentation with MCP");
        if (isSearchResultEmpty(singleTicketSearchResults)) {
            return;
        }
        List<String> multiTicketSearchKeywords = parseRelatedTicketKeywords(singleTicketSearchResults.get(0).fields.description);

        var multiTicketSearchProjectName = "EPM-CDME";
        List<JiraSearchResponseIssue> foundJiraIssues = findRelatedTickets(multiTicketSearchProjectName, multiTicketSearchKeywords);

        transformJiraTicketsToRequirements(foundJiraIssues);
    }

    /**
     * Retrieves provided ticket description, searches the ticket by projectName and jiraSearchText
     */
    private List<JiraSearchResponseIssue> retrieveProvidedTicketDescription(String projectName, String jiraSearchText) {
        JiraSearchResponse singleTicketSearchResponse = jiraSearchService.findSingleTicketBySummary(projectName, jiraSearchText);
        return singleTicketSearchResponse.getIssues();
    }

    private List<String> parseRelatedTicketKeywords( String singleJiraTicketDescription) {
        var relatedTicketKeywordsAsText = StringUtils.substringAfter(singleJiraTicketDescription, RELATED_TICKETS_PREFIX);
        List<String> relatedTicketKeywords = List.of(StringUtils.split(relatedTicketKeywordsAsText, ","));
        return relatedTicketKeywords;
    }

    /**
     * Finds related tickets based on search text
     */
    private List<JiraSearchResponseIssue> findRelatedTickets(String projectName, List<String> keywords) {
        var jiraSearchResponse = jiraSearchService.findMultipleTicketsByKeywords(projectName, keywords);
        for (JiraSearchResponseIssue searchResponseIssue : jiraSearchResponse.getIssues()) {
            log.info("\nFound related ticket Key: {}, ticket Summary: {}", searchResponseIssue.key, searchResponseIssue.fields.summary);
        }
        return jiraSearchResponse.getIssues();
    }

    private String transformJiraTicketsToRequirements(List<JiraSearchResponseIssue> jiraIssues) {
        var generatedRequirementsResponse = llmApiService.callChatCompletionApi(jiraIssues);
        log.info("\n<----- Generated requirements: ------\n\n" + generatedRequirementsResponse);
        return generatedRequirementsResponse;
    }

    private static boolean isSearchResultEmpty(List<JiraSearchResponseIssue> singleTicketSearchResults) {
        if (singleTicketSearchResults.isEmpty()) {
            log.error("No ticket found for the provided search text = {}", "experimentation with MCP");
            return true;
        }
        return false;
    }

}
