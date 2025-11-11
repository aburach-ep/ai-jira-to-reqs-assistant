package com.ai.poc.agent.llm.service;

import com.ai.poc.agent.jira.dto.JiraSearchResponseTicket;
import com.ai.poc.agent.llm.dto.GeminiGenerateContentRequestDto;
import com.ai.poc.agent.llm.dto.LlmChatCompletionRequestDto;
import com.google.common.collect.Lists;

import java.util.List;

public class SapLlmRequestBuilder {

    // gpt-4-o supports max 4096 completion tokens
    static final int MAX_COMPLETION_TOKENS = 4000;
    static final String LLM_REQUEST_ROLE_USER = "user";
    static final String FOUNDATION_MODEL_GEMINI_25_PRO = "gemini-2.5-pro";

    public static LlmChatCompletionRequestDto createLlmRequestUsingGpt4oModel(String requirementsGenerationInstruction) {
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

    public static GeminiGenerateContentRequestDto createLlmRequestUsingGemini25ProModel(String requirementsGenerationInstruction) {
        return GeminiGenerateContentRequestDto.builder()
                .model(FOUNDATION_MODEL_GEMINI_25_PRO)
                .contents(
                        Lists.newArrayList(
                                GeminiGenerateContentRequestDto.Content.builder()
                                        .role(LLM_REQUEST_ROLE_USER)
                                        .parts(
                                                Lists.newArrayList(
                                                        GeminiGenerateContentRequestDto.Part.builder()
                                                                .text(requirementsGenerationInstruction).build()
                                                )
                                        ).build()
                        )
                )
                .build();
    }

}
