package com.ai.poc.agent.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.ai.poc.agent.llm.client.SapLlmAuthClient;
import com.ai.poc.agent.llm.client.SapLlmClient;
import com.ai.poc.agent.llm.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SapLlmApiService {

    public static final String SAP_LLM_AUTH_GRANT_TYPE = "client_credentials";
    public static final String FOUNDATION_MODEL_GPT4O = "gpt-4o";
    public static final String FOUNDATION_MODEL_GEMINI_25_PRO = "gemini-2.5-pro";

    private final SapLlmAuthClient sapLlmAuthClient;
    private final SapLlmClient sapLlmClient;

    @Value("${sap.llm.auth.client-id}")
    private String clientId;

    @Value("${sap.llm.auth.client-secret}")
    private String clientSecret;

    @Value("${sap.llm.foundation-model-to-use:gpt-4o}")
    private String foundationModelToUse;

    @Value("${sap.llm.gpt4o-deployment-id}")
    private String gpt4oDeploymentId;

    @Value("${sap.llm.gpt4o-api-version:2024-10-21}")
    private String gpt4oApiVersion;

    @Value("${sap.llm.gemini25-pro-deployment-id}")
    private String gemini25ProDeploymentId;

    @Value("${sap.llm.gemini25-pro-api-version:gemini-2.5-pro}")
    private String gemini25ProApiVersion;


    /**
     * Method accepts Jira tickets, parsed from CSV file
     * @param jiraTickets
     * @return llmResponseMessage
     */
    public String callChatCompletionApiForExportFileTickets(List<JiraTicketDto> jiraTickets) {
        // 1. Get access token
        String accessToken = fetchAccessTokenViaApiCall();
        var llmResponseMessage = "";

        // 2. Call LLM api endpoint
        switch (foundationModelToUse) {
            case FOUNDATION_MODEL_GEMINI_25_PRO: {
                llmResponseMessage = callLlmApiUsingGemini25ProAndExportFileTickets(accessToken, jiraTickets);
                break;
            }
            case FOUNDATION_MODEL_GPT4O:
            default: {
                llmResponseMessage = callLlmApiUsingGpt4oAndExportFileTickets(accessToken, jiraTickets);
            }
        }
        return llmResponseMessage;
    }


    /**
     * Method accepts Jira Search Response tickets
     * @param jiraTickets
     * @return llmResponseMessage
     */
    public String callChatCompletionApiForSearchResultTickets(List<JiraSearchResponseTicket> jiraTickets) {
        // 1. Get access token
        String accessToken = fetchAccessTokenViaApiCall();
        var llmResponseMessage = "";

        // 2. Call LLM api endpoint
        switch (foundationModelToUse) {
            case FOUNDATION_MODEL_GEMINI_25_PRO: {
                llmResponseMessage = callLlmApiUsingGemini25ProAndSearchResTickets(accessToken, jiraTickets);
                break;
            }
            case FOUNDATION_MODEL_GPT4O:
            default: {
                llmResponseMessage = callLlmApiUsingGpt4oAndSearchResTickets(accessToken, jiraTickets);
            }
        }
        return llmResponseMessage;
    }

    private String fetchAccessTokenViaApiCall() {
        SapLlmAuthTokenResponseDto tokenResponse = sapLlmAuthClient.getAccessToken(
                SAP_LLM_AUTH_GRANT_TYPE,
                clientId,
                clientSecret
        );
        String accessToken = "Bearer " + tokenResponse.getAccessToken();
        return accessToken;
    }

    private String callLlmApiUsingGpt4oAndExportFileTickets(String accessToken, List<JiraTicketDto> jiraTickets) {
        var requirementsGenerationInstruction = JiraTicketsToLlmInstructionConverter.mapJiraExportFileIssuesToFormattedLlmInstruction(jiraTickets);
        LlmChatCompletionRequestDto llmRequest = SapLlmRequestBuilder.createLlmRequestUsingGpt4oModel(requirementsGenerationInstruction);
        LlmChatCompletionResponseDto llmResponse = sapLlmClient.callGpt4oChatCompletionApi(
                gpt4oDeploymentId, gpt4oApiVersion, accessToken, llmRequest
        );
        return llmResponse.getChoices().get(0).getMessage().getContent();
    }

    private String callLlmApiUsingGemini25ProAndExportFileTickets(String accessToken, List<JiraTicketDto> jiraTickets) {
        var requirementsGenerationInstruction = JiraTicketsToLlmInstructionConverter.mapJiraExportFileIssuesToFormattedLlmInstruction(jiraTickets);
        GeminiGenerateContentRequestDto llmRequest = SapLlmRequestBuilder.createLlmRequestUsingGemini25ProModel(requirementsGenerationInstruction);
        GeminiGenerateContentResponseDto llmResponse = sapLlmClient.callGemini25ProGenerateContentApi(
                gemini25ProDeploymentId, gemini25ProApiVersion, accessToken, llmRequest
        );
        return  llmResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

    private String callLlmApiUsingGpt4oAndSearchResTickets(String accessToken, List<JiraSearchResponseTicket> jiraTickets) {
        var requirementsGenerationInstruction = JiraTicketsToLlmInstructionConverter.mapJiraSearchIssuesToFormattedLlmInstruction(jiraTickets);
        LlmChatCompletionRequestDto llmRequest = SapLlmRequestBuilder.createLlmRequestUsingGpt4oModel(requirementsGenerationInstruction);
        LlmChatCompletionResponseDto llmResponse = sapLlmClient.callGpt4oChatCompletionApi(
                gpt4oDeploymentId, gpt4oApiVersion, accessToken, llmRequest
        );
        return llmResponse.getChoices().get(0).getMessage().getContent();
    }

    private String callLlmApiUsingGemini25ProAndSearchResTickets(String accessToken, List<JiraSearchResponseTicket> jiraTickets) {
        var requirementsGenerationInstruction = JiraTicketsToLlmInstructionConverter.mapJiraSearchIssuesToFormattedLlmInstruction(jiraTickets);
        GeminiGenerateContentRequestDto llmRequest = SapLlmRequestBuilder.createLlmRequestUsingGemini25ProModel(requirementsGenerationInstruction);
        GeminiGenerateContentResponseDto llmResponse = sapLlmClient.callGemini25ProGenerateContentApi(
                gemini25ProDeploymentId, gemini25ProApiVersion, accessToken, llmRequest
        );
        return  llmResponse.getCandidates().get(0).getContent().getParts().get(0).getText();
    }

}
