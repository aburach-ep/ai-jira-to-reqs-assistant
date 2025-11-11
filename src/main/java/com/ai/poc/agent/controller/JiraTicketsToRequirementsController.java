package com.ai.poc.agent.controller;

import com.ai.poc.agent.service.JiraExcelFileToRequiremTransformerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;

@RestController
@RequiredArgsConstructor
public class JiraTicketsToRequirementsController {

    private final JiraExcelFileToRequiremTransformerService jiraExcelFileToRequiremTransformerService;

    // the endpoint expects Exported CSV file with Jira tickets as attachment
	@PostMapping(
			value = "/jira-tickets-to-requirements",
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
	)
    public String parseJiTicketsFromFileAndTransformToRequirements(@RequestPart("file") MultipartFile jiraTicketsCsvFile) {
		String jiraTicketsCsvFileContent = readFileToString(jiraTicketsCsvFile);
		return jiraExcelFileToRequiremTransformerService.parseJiraTicketsFromExcelAndTransformToRequirements(jiraTicketsCsvFileContent);
    }

	private static String readFileToString(MultipartFile file) {
		try {
			return new String(file.getBytes(), StandardCharsets.UTF_8);
		} catch (Exception e) {
			throw new RuntimeException("Failed to read attached to request CSV file", e);
		}
	}
} 