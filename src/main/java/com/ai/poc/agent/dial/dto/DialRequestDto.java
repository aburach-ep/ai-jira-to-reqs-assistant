package com.ai.poc.agent.dial.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DialRequestDto {

    private List<DialRequestMessage> messages;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DialRequestMessage {

        private String role;
        private String content;

    }

}
