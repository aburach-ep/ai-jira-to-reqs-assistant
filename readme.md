# AI Agent, which transforms Jira ticket(s) description to requirements

This project provides AI-powered assistant (agent), capable to generate holistic requirements for provided Jira ticket(s).

## High-level agent Overview
- searches for related tickets using the "{*}Related Tickets{*}: {keyword1, keywordN}" keywords within the ticket description (i.e. "Related Tickets" should in bold, or otherwise the genAI assistant won't find related tickets).
  This search phrase could be replaced by usage of e.g. Jira **labels**.
- transforms found related Jira tickets description to holistic requirements using AI provider (LLM)

## Getting Started
1. Update your Jira and AI (LLM) API keys in `application.yaml`.
2. Run `./gradlew bootRun` to start the service.
3. Experiment with the agent by accessing: [http://localhost:8080/agent/requirements?project=PROJECT_KEY&searchText=SEARCH_TEXT](http://localhost:8080/agent/requirements?project=PROJECT_KEY&searchText=SEARCH_TEXT)

## Notes
- Built with Java 21, Spring Boot, and Gradle

## Contributing & Extending
If you need:
- Unit tests
- Dockerfile
- Further integrations (other systems)
- Additional features

Feel free to open an issue or submit a pull request!