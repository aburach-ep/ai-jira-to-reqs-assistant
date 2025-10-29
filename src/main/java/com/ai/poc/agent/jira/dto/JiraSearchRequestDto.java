package com.ai.poc.agent.jira.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraSearchRequestDto {
    private String jql;
    private int startAt;
    private int maxResults;

}
