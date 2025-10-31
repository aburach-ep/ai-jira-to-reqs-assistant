package com.ai.poc.agent.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseIssue;
import com.ai.poc.agent.llm.client.SapLlmAuthClient;
import com.ai.poc.agent.llm.client.SapLlmClient;
import com.ai.poc.agent.llm.dto.LlmChatCompletionRequestDto;
import com.ai.poc.agent.llm.dto.LlmChatCompletionResponseDto;
import com.ai.poc.agent.llm.dto.SapLlmAuthTokenResponseDto;
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

    private final SapLlmAuthClient sapLlmAuthClient;
    private final SapLlmClient sapLlmClient;

    @Value("${sap.llm.auth.client-id}")
    private String clientId;

    @Value("${sap.llm.auth.client-secret}")
    private String clientSecret;

    @Value("${sap.llm.deploymentId}")
    private String deploymentId;

    @Value("${sap.llm.api-version:2024-10-21}")
    private String apiVersion;

    public String callChatCompletionApi(List<JiraSearchResponseIssue> jiraTickets) {
        // 1. Get access token
        SapLlmAuthTokenResponseDto tokenResponse = sapLlmAuthClient.getAccessToken(
                SAP_LLM_AUTH_GRANT_TYPE,
                clientId,
                clientSecret
        );
        String accessToken = "Bearer " + tokenResponse.getAccessToken();
        LlmChatCompletionRequestDto llmRequest = SapLlmRequestBuilder.createLlmRequestUsingGpt4oModel(jiraTickets);

        // 2. Call LLM completions endpoint
        LlmChatCompletionResponseDto llmResponse = sapLlmClient.callChatCompletionApi(
            deploymentId, apiVersion, accessToken, llmRequest
        );
        var llmResponseMessage = llmResponse.getChoices().get(0).getMessage().getContent();
        return llmResponseMessage;
    }
}
