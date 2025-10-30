package com.ai.poc.agent.llm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.ai.poc.agent.llm.dto.LlmChatCompletionRequestDto;
import com.ai.poc.agent.llm.dto.LlmChatCompletionResponseDto;

@FeignClient(name = "llmClient", url = "${sap.llm.endpoint}")
public interface SapLlmClient {

    @PostMapping("/v2/inference/deployments/{deploymentId}/chat/completions")
    LlmChatCompletionResponseDto callChatCompletionApi(
            @PathVariable("deploymentId") String deploymentId,
            @RequestParam("api-version") String apiVersion,
            @RequestBody LlmChatCompletionRequestDto request
    );
}
