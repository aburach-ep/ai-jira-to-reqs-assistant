package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.ai.poc.agent.utils.FileUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JiraExcelParseServiceTest {

    @Test
    void testParseJiraTicketsFromCsvWhereOddColumnsRemoved() throws Exception {
        String excelWithJiraTickets = FileUtils.readSystemResource("jira-data/ARTDATA-5024-w-SubTickets-short-odd-columns-remvd.csv");
        List<JiraTicketDto> jiraTicketsDto = JiraExcelParseService.parseJiraTicketsFromCsv(excelWithJiraTickets);

        Assertions.assertThat(jiraTicketsDto).isNotEmpty();
        Assertions.assertThat(jiraTicketsDto).extracting(jiraTicketDto -> jiraTicketDto.getIssueKey())
                .isNotNull();
    }

}
