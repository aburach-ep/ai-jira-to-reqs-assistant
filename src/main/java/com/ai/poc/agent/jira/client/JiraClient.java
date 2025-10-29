package com.ai.poc.agent.jira.client;

import com.ai.poc.agent.jira.config.FeignConfig;
import com.ai.poc.agent.jira.config.JiraClientConfig;
import com.ai.poc.agent.jira.dto.JiraSearchRequestDto;
import com.ai.poc.agent.jira.dto.JiraSearchResponse;
import feign.Headers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
    name = "jiraClient",
    url = "${jira.base-url}/rest/api/2",
    configuration = JiraClientConfig.class
)
public interface JiraClient {

    @PostMapping(path = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    JiraSearchResponse findItems(@RequestBody JiraSearchRequestDto request);
} 