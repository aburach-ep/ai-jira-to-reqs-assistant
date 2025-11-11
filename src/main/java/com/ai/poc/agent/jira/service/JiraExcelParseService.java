package com.ai.poc.agent.jira.service;

import com.ai.poc.agent.jira.dto.JiraTicketDto;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class JiraExcelParseService {

    private static final String CSV_COLUMN_SUMMARY = "Summary";
    private static final String CSV_COLUMN_ISSUE_KEY = "Issue key";
    private static final String CSV_COLUMN_ISSUE_TYPE = "Issue Type";
    private static final String CSV_COLUMN_STATUS = "Status";
    private static final String CSV_COLUMN_PROJECT_KEY = "Project key";
    private static final String CSV_COLUMN_DESCRIPTION = "Description";
//    private static final String CSV_COLUMN_SPRINT = "Sprint";
//    private static final String CSV_COLUMN_COMPONENT = "Component/s";

    private static final List<String> CSV_COLUMN_NAMES = List.of(
            CSV_COLUMN_SUMMARY, CSV_COLUMN_ISSUE_KEY, CSV_COLUMN_ISSUE_TYPE, CSV_COLUMN_STATUS, CSV_COLUMN_PROJECT_KEY,
            CSV_COLUMN_DESCRIPTION/*, CSV_COLUMN_SPRINT, CSV_COLUMN_COMPONENT*/);

    private static final String ZERO_WIDTH_NO_BREAK_SPACE_UNICODE_CHAR = "\uFEFF";

    /**
     * // todo: this should be moved to the calling client level
    *   String jiraTicketsCsvFileContent = FileUtils.readSystemResource(resourcePath);
     */
    public static List<JiraTicketDto> parseJiraTicketsFromCsv(String jiraTicketsCsvFileContent) {
        List<JiraTicketDto> tickets = new ArrayList<>();

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setTrim(true)
                .setIgnoreEmptyLines(false)
                .build();
        try (StringReader reader = new StringReader(jiraTicketsCsvFileContent);
             CSVParser parser = new CSVParser(reader, csvFormat)) {
            List<CSVRecord> records = parser.getRecords();
            if (records.isEmpty()) {
                log.warn("CSV file appeared to be empty or could not be parsed");
                return tickets;
            }
            // First record is the header - extract Sprint column indices
            CSVRecord headerRecord = records.get(0);
            Map<String, Integer> columnIndexes = findColumnIndices(headerRecord, CSV_COLUMN_NAMES);

            // Parse data records (and skip header)
            for (int i = 1; i < records.size(); i++) {
                CSVRecord record = records.get(i);
                try {
                    JiraTicketDto ticket = mapRecordToDto(record, columnIndexes);
                    if (ticket != null && StringUtils.isNotBlank(ticket.getIssueKey())) {
                        tickets.add(ticket);
                    }
                } catch (Exception e) {
                    log.error("Error parsing CSV record at line {}: {}", i + 1, e.getMessage(), e);
                }
            }
        } catch (IOException e) {
            log.error("Error parsing jiraTicketsCsvFileContent: {}", jiraTicketsCsvFileContent, e);
            throw new RuntimeException("Failed to parse jiraTicketsCsvFileContent: " + jiraTicketsCsvFileContent, e);
        }

        log.info("Successfully parsed {} Jira tickets from CSV file", tickets.size());
        return tickets;
    }

    /**
     * Finds all column indices that match the given column name.
     *
     * @param headerRecord The header record
     * @param columnNames  Column names to search for
     * @return List of column indices
     */
    private static Map<String, Integer> findColumnIndices(CSVRecord headerRecord, List<String> columnNames) {
        Map<String, Integer> columnNamesToIndices = Maps.newHashMapWithExpectedSize(columnNames.size());
        for (int i = 0; i < headerRecord.size(); i++) {
            String headerColumnValue = headerRecord.get(i);
            String headerValue = StringUtils.replace(headerColumnValue, ZERO_WIDTH_NO_BREAK_SPACE_UNICODE_CHAR, "");
            if (columnNames.contains(headerValue)) {
                columnNamesToIndices.put(headerValue, i);
            }
        }
        return columnNamesToIndices;
    }

    /**
     * Maps a CSV record to a JiraTicketDto object.
     *
     * @param record       The CSV record to map
     * @param columnIndexes Map of CSV column names to indexes
     * @return JiraTicketDto object or null if mapping fails
     */
    private static JiraTicketDto mapRecordToDto(CSVRecord record, Map<String, java.lang.Integer> columnIndexes) {
        try {
            String summary = getValueSafely(record, columnIndexes.get(CSV_COLUMN_SUMMARY));
            String issueKey = getValueSafely(record, columnIndexes.get(CSV_COLUMN_ISSUE_KEY));
            String issueType = getValueSafely(record, columnIndexes.get(CSV_COLUMN_ISSUE_TYPE));
            String status = getValueSafely(record, columnIndexes.get(CSV_COLUMN_STATUS));
            String projectKey = getValueSafely(record, columnIndexes.get(CSV_COLUMN_PROJECT_KEY));
            String description = getValueSafely(record, columnIndexes.get(CSV_COLUMN_DESCRIPTION));

            return JiraTicketDto.builder()
                    .issueKey(issueKey)
                    .summary(summary)
                    .issueType(issueType)
                    .status(status)
                    .projectKey(projectKey)
                    .description(description)
                    .build();
        } catch (Exception e) {
            log.error("Error mapping CSV record to JiraTicketDto: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Gets the value at the specified index, returning empty string if index is out of bounds.
     */
    private static String getValueSafely(CSVRecord record, Integer index) {
        if (index == null)
            return StringUtils.EMPTY;
        if (index >= 0 && index < record.size()) {
            String value = record.get(index);
            return value != null ? value.trim() : StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }

}