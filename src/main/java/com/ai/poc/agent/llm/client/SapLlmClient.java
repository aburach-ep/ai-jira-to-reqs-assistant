package com.ai.poc.agent.llm.client;

import com.ai.poc.agent.llm.dto.GeminiGenerateContentRequestDto;
import com.ai.poc.agent.llm.dto.GeminiGenerateContentResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
import com.ai.poc.agent.llm.dto.LlmChatCompletionRequestDto;
import com.ai.poc.agent.llm.dto.LlmChatCompletionResponseDto;

@FeignClient(name = "llmClient", url = "${sap.llm.endpoint}")
public interface SapLlmClient {

    @PostMapping("/v2/inference/deployments/{deploymentId}/chat/completions")
    LlmChatCompletionResponseDto callGpt4oChatCompletionApi(
            @PathVariable("deploymentId") String deploymentId,
            @RequestParam("api-version") String apiVersion,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody LlmChatCompletionRequestDto request
    );

    @PostMapping("/v2/inference/deployments/{deploymentId}/models/{apiVersion}:generateContent")
    GeminiGenerateContentResponseDto callGemini25ProGenerateContentApi(
            @PathVariable("deploymentId") String deploymentId,
            @PathVariable("apiVersion") String apiVersion,
            @RequestHeader("Authorization") String authHeader,
            @RequestBody GeminiGenerateContentRequestDto request
    );
}
