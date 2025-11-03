package com.ai.poc.agent.controller;

import com.ai.poc.agent.jira.dto.JiraTicketAnalysisResultDto;
import com.ai.poc.agent.jira.service.JiraTicketAnalysisService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
//todo: refactor this class to start analysis for particular Jira ticket
public class JiraTicketController {

    private final JiraTicketAnalysisService jiraTicketAnalysisService;

    public JiraTicketController(JiraTicketAnalysisService jiraTicketAnalysisService) {
        this.jiraTicketAnalysisService = jiraTicketAnalysisService;
    }

    // TODO: update endpoint to POST with just projectName and searchText as request body
    @GetMapping("/tickets/summaries")
    public List<JiraTicketAnalysisResultDto> getTicketSummaries() {
        return jiraTicketAnalysisService.analyzeTickets("EPMCDMETST", "ai agent");
    }
} 