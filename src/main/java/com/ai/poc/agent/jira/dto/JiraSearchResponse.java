package com.ai.poc.agent.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
// This is DTO for Jira search response
public class JiraSearchResponse {

    @JsonProperty("maxResults")
    private int maxResults;

    private List<JiraSearchResponseIssue> issues;

}
