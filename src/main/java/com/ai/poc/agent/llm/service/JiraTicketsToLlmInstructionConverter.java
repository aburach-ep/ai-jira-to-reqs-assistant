package com.ai.poc.agent.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.ai.poc.agent.utils.FileUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class JiraTicketsToLlmInstructionConverter {

    public static String mapJiraExportFileIssuesToFormattedLlmInstruction(List<JiraTicketDto> jiraTickets)  {
        var jiraTicketDescriptions = new StringBuilder();
        for (int i = 1; i <= jiraTickets.size(); i++) {
            var jiraTicket = jiraTickets.get(i - 1);   // numbering should start from 1
            jiraTicketDescriptions.append("Jira ticket # " + jiraTicket.getIssueKey()).append("\n");
            jiraTicketDescriptions.append("\t" + "Jira ticket Summary: " + jiraTicket.getSummary()).append("\n");
            var formattedDescription = appendTabForEachNewLine(jiraTicket.getDescription());
            jiraTicketDescriptions.append("\t" + "Jira ticket Description: \n\t\t" + formattedDescription).append("\n");
        }
        var llmPrompt = FileUtils.readSystemResource("/ai-prompts/requirem-generation-instruction.txt");
        return llmPrompt.concat(jiraTicketDescriptions.toString());
    }

    public static String mapJiraSearchIssuesToFormattedLlmInstruction(List<JiraSearchResponseTicket> jiraTickets)  {
        var jiraTicketDescriptions = new StringBuilder();
        for (int i = 1; i <= jiraTickets.size(); i++) {
            var jiraTicket = jiraTickets.get(i - 1);   // numbering should start from 1
            jiraTicketDescriptions.append("Jira ticket # " + jiraTicket.getKey()).append("\n");
            jiraTicketDescriptions.append("\t" + "Jira ticket Summary: " + jiraTicket.getFields().summary).append("\n");
            var formattedDescription = appendTabForEachNewLine(jiraTicket.getFields().description);
            jiraTicketDescriptions.append("\t" + "Jira ticket Description: \n\t\t" + formattedDescription).append("\n");
        }
        var llmPrompt = FileUtils.readSystemResource("/ai-prompts/requirem-generation-instruction.txt");
        return llmPrompt.concat(jiraTicketDescriptions.toString());
    }

    private static String appendTabForEachNewLine(String ticketDescription) {
        return StringUtils.replace(ticketDescription, "\n", "\n\t\t");
    }

}
