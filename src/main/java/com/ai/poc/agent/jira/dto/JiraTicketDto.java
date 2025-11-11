package com.ai.poc.agent.jira.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraTicketDto {
    
    // jira task or jira issue name
    private String issueKey;
    private String summary;
    private String issueType;
    private String status;
    private String projectKey;
    private String description;

}
