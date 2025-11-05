package com.ai.poc.agent.controller;

import com.ai.poc.agent.service.JiraToRequirementsTransformerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class JiraTicketsToRequirementsController {

    private final JiraToRequirementsTransformerService jiraToRequirementsTransformerService;

    // TODO: update endpoint to POST with just projectName and searchText as request body
    @PostMapping(value = "/jira-tickets-to-requirements", produces = MediaType.APPLICATION_JSON_VALUE)
    public String findRelatedTicketsAndTransformToRequirements(
            @RequestParam("parentFeatureTicketKey") String parentFeatureTicketKey
    ) {
        // parentFeatureTicketKey will look like EPMCDMETST-14052
        return jiraToRequirementsTransformerService.findAndTransformJiraTicketsToRequirements(parentFeatureTicketKey);
    }
} 