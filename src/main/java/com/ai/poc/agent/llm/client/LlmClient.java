package com.ai.poc.agent.llm.client;

import com.ai.poc.agent.dial.dto.DialRequestDto;
import com.ai.poc.agent.dial.dto.DialResponseDto;
import com.ai.poc.agent.jira.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
    name = "dialClient",
    url = "${dial.endpoint}/${dial.foundation-model}", configuration = FeignConfig.class
)
//TODO: implement as needed
public interface LlmClient {
    @PostMapping(path = "/chat/completions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    DialResponseDto callChatApi(@RequestHeader("Api-Key") String apiKey,
                                @RequestParam(name = "api-version") String apiVersion,
                                @RequestBody DialRequestDto request);
} 