package com.ai.poc.agent.jira.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class JiraSearchResponseIssue {
    public String key;

    @JsonProperty("fields")
    public JiraIssueFields fields;

    public static class JiraIssueFields {

        // jira task or jira issue name
        public String summary;

        @JsonProperty("description")
        public String description;
    }

}