package com.ai.poc.agent.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.ai.poc.agent.jira.service.JiraExcelParseService;
import com.ai.poc.agent.llm.service.SapLlmApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JiraExcelFileToRequiremTransformerService {

    private final SapLlmApiService llmApiService;

    public String parseJiraTicketsFromExcelAndTransformToRequirements(String jiraTicketsCsvFileContent) {
        List<JiraTicketDto> jiraTickets = JiraExcelParseService.parseJiraTicketsFromCsv(jiraTicketsCsvFileContent);
        if (isSearchResultEmpty(jiraTicketsCsvFileContent)) {
            return StringUtils.EMPTY;
        }

        return transformJiraTicketsToRequirements(jiraTickets);
    }


    private String transformJiraTicketsToRequirements(List<JiraTicketDto> jiraTickets) {
        var generatedRequirementsResponse = llmApiService.callChatCompletionApiForExportFileTickets(jiraTickets);
        log.info("\n<----- Generated requirements: ------\n\n" + generatedRequirementsResponse);
        return generatedRequirementsResponse;
    }

    private static boolean isSearchResultEmpty(String jiraTicketsFileContent) {
        if (StringUtils.isEmpty(jiraTicketsFileContent)) {
            log.error("Failed to parse any Jira tickets in provided file, fileContent = {}. Skipping further processing", jiraTicketsFileContent);
            return true;
        }
        return false;
    }

}
