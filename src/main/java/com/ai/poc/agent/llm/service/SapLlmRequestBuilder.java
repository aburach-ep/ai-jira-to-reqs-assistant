package com.ai.poc.agent.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.llm.dto.LlmChatCompletionRequestDto;
import com.ai.poc.agent.utils.FileUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

public class SapLlmRequestBuilder {

    // gpt-4-o supports max 4096 completion tokens
    static final int MAX_COMPLETION_TOKENS = 4000;
    static final String LLM_REQUEST_ROLE_USER = "user";

    public static LlmChatCompletionRequestDto createLlmRequestUsingGpt4oModel(List<JiraSearchResponseIssue> jiraTickets) {
        var requirementsGenerationInstruction = transformJiraIssuesToFormattedLlmInstruction(jiraTickets);
        return LlmChatCompletionRequestDto.builder()
                .messages(Lists.newArrayList(
                        LlmChatCompletionRequestDto.Message.builder()
                                .role(LLM_REQUEST_ROLE_USER)
                                .content(requirementsGenerationInstruction)
                                .build()
                ))
                .max_completion_tokens(MAX_COMPLETION_TOKENS)
                .build();
    }

    public static String transformJiraIssuesToFormattedLlmInstruction(List<JiraSearchResponseIssue> jiraIssues)  {
        var jiraTicketDescriptions = new StringBuilder();
        for (int i = 1; i <= jiraIssues.size(); i++) {
            var jiraItem = jiraIssues.get(i - 1);   // numbering should start from 1
            jiraTicketDescriptions.append("Jira ticket # " + i).append("\n");
            jiraTicketDescriptions.append("\t" + "Jira ticket Summary: " + jiraItem.getFields().summary).append("\n");
            var formattedDescription = appendTabForEachNewLine(jiraItem.getFields().description);
            jiraTicketDescriptions.append("\t" + "Jira ticket Description: \n\t" + formattedDescription).append("\n");
        }
        var llmPrompt = FileUtils.readSystemResource("/ai-prompts/requirem-generation-instruction.txt");
        return llmPrompt.concat(jiraTicketDescriptions.toString());
    }

    private static String appendTabForEachNewLine(String ticketDescription) {
        return StringUtils.replace(ticketDescription, "\n", "\n\t");
    }

}
