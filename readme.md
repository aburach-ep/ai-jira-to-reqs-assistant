# AI Agent, which transforms Jira ticket(s) description to requirements

This project provides AI-powered assistant (agent), capable to generate holistic well-defined, structured requirements for provided Jira tickets' descriptions.

## High-level agent Overview
- this genAi assistant exposes REST endpoint `/jira-tickets-to-requirements`, expecting exported from Jira CSV file with appropriate related Jira tickets.
- the CSV file (which can be obtained via export from Jira) should contain **at least the following columns**: 
  - `Summary`, `Issue key`, `Issue Type`, `Description`
- due certain customer infrastructure limitations this genAi agent currently expects related tickets in form of CSV file, exportde from Jira. In future, once there's posibbility to call Jira REST api programmatically, the genAI assisant could be extended to search for related Jira tickets using the "{*}Related Tickets{*}: {keyword1, keywordN}" keywords within the ticket description, or using e.g. same **Component** and **labels**.
  This search phrase could be replaced by usage of e.g. Jira **labels**.
- target Foundation Model is configurable, currently these FMs are supported:
  - gpt-4o
  - gemini-2.5-pro

## Getting Started
1. Update your Jira and AI (LLM) API keys in `application.yaml`.
2. Run `./gradlew bootRun` to start the service.
3. Sample request:
   ```bash
   curl -X POST "http://localhost:8080/jira-tickets-to-requirements" -H "Accept: application/json"  -F "file=@/path/to/jira_tickets.csv"
   ```

## Notes
- Built with Java 21, Spring Boot, and Gradle

Feel free to open an issue or submit a pull request!