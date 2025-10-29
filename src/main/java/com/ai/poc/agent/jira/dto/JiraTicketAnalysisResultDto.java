package com.ai.poc.agent.jira.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JiraTicketAnalysisResultDto {

    @JsonProperty("ticketKey")
    private String ticketKey;

    @JsonProperty("completenessEvaluation")
    private String completenessEvaluation;

    @JsonProperty("ticketSummary")
    private String ticketSummary;

    @JsonProperty("potentialQuestions")
    private String potentialQuestions;

}
