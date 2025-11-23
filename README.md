<div align="center">

<img src=".readme/spring-boot.svg" alt="Spring Boot"/>

# Spring Boot and AI Quickstarts

[![GitHub Build](https://img.shields.io/github/actions/workflow/status/loicgreffier/spring-boot-ai-quickstarts/build.yml?branch=main&logo=github&style=for-the-badge)](https://github.com/loicgreffier/spring-boot-ai-quickstarts/actions/workflows/build.yml)
[![Spring Boot Version](https://img.shields.io/badge/dynamic/xml?url=https%3A%2F%2Fraw.githubusercontent.com%2Floicgreffier%2Fspring-boot-ai-quickstarts%2Fmain%2Fpom.xml&query=%2F*%5Blocal-name()%3D'project'%5D%2F*%5Blocal-name()%3D'parent'%5D%2F*%5Blocal-name()%3D'version'%5D%2Ftext()&style=for-the-badge&logo=spring-boot&label=version)](https://github.com/loicgreffier/spring-boot-ai-quickstarts/blob/main/pom.xml)
[![GitHub Stars](https://img.shields.io/github/stars/loicgreffier/spring-boot-ai-quickstarts?logo=github&style=for-the-badge)](https://github.com/loicgreffier/spring-boot-ai-quickstarts)
[![Docker Pulls](https://img.shields.io/docker/pulls/loicgreffier/spring-boot-ai-quickstarts?label=Pulls&logo=docker&style=for-the-badge)](https://hub.docker.com/r/loicgreffier/spring-boot-ai-quickstarts/tags)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg?logo=apache&style=for-the-badge)](https://opensource.org/licenses/Apache-2.0)

[Chat](#chat) • [MCP](#mcp)

Code samples around Spring AI.

</div>

## Prerequisites

- Java 25
- Maven
- Node.js 24
- NPM
- Ollama
- Docker

## Quickstarts

### Chat

| Module                                          | Description                                                      |
|-------------------------------------------------|------------------------------------------------------------------|
| [Chat](/chat)                                   | Synchronous chatbot                                              |
| [Chat Memory In-Memory](/chat-memory-in-memory) | Chatbot with in-memory conversation storage                      |
| [Chat Memory JDBC](/chat-memory-jdbc)           | Chatbot with JDBC (PostgreSQL) conversation storage              |
| [Chat RAG](/chat-rag)                           | Chatbot with retrieval-augmented generation (RAG)                |
| [Chat RAG Advanced](/chat-rag-advanced)         | Chatbot with advanced retrieval-augmented generation (RAG) setup |
| [Chat Stream](/chat-stream)                     | Streaming chatbot                                                |

### MCP

| Module                                          | Description                                                      |
|-------------------------------------------------|------------------------------------------------------------------|
| [MCP Server Stdio](/mcp-server-stdio)           | MCP server with standard I/O interaction                         |

## Credits

[Spring AI Documentation](https://docs.spring.io/spring-ai/reference/index.html)

[The Simpsons API](https://thesimpsonsapi.com)