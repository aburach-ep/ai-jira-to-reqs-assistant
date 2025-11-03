package com.ai.poc.agent.llm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeminiGenerateContentRequestDto {

    private String model;
    private List<Content> contents;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Content {
        private String role; // e.g., "user"
        private List<Part> parts;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Part {
        private String text;
    }
}
