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
public class LlmChatCompletionRequestDto {

    private List<Message> messages;
    private int max_completion_tokens;

    @Data
    @Builder
    public static class Message {
        private String role;
        private String content;
    }
}
