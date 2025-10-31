package com.ai.poc.agent.llm.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ai.poc.agent.llm.dto.SapLlmAuthTokenResponseDto;

@FeignClient(
        name = "sapLlmAuthClient",
        url = "${sap.llm.auth.url}")
public interface SapLlmAuthClient {

    @PostMapping(value = "/oauth/token", consumes = "application/x-www-form-urlencoded")
    SapLlmAuthTokenResponseDto getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret
    );
}
