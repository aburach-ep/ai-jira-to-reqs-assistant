package com.ai.poc.agent.jira.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;


@Data
public class JiraSearchResponseTicket {
    public String key;

    @JsonProperty("fields")
    public JiraTicketFields fields;

    public static class JiraTicketFields {

        // jira task or jira issue title
        public String summary;

        @JsonProperty("description")
        public String description;
    }

}