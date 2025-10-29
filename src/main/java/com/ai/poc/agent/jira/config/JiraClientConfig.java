package com.ai.poc.agent.jira.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

/**
 * @Configuration annotation is intentionally omitted for this class to prevent it being applied application-wide,
 * as this config is purposed for JiraClient only.
 */
public class JiraClientConfig {

    @Value("${jira.api-token}")
    private String jiraApiToken;

    @Bean
    public RequestInterceptor bearerTokenInterceptor() {
        return new JiraBearerTokenInterceptor(jiraApiToken);
    }
}

@RequiredArgsConstructor
class JiraBearerTokenInterceptor implements RequestInterceptor {

    private final String jiraApiToken;

    @Override
    public void apply(RequestTemplate template) {
        template.header("Authorization", "Bearer " + jiraApiToken);
    }

}