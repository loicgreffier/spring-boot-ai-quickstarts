# MCP Server Stdio

This module demonstrates how to create an MCP server using standard I/O (stdio) communication.
It uses data from [The Simpsons Character API](https://thesimpsonsapi.com), which is pre-fetched anf formatted in `data.sql` and stored in an in-memory H2 database when the application starts.

It exposes the following tools:
- `get_characters_by_name`, which returns information about Simpsons characters based on a given partial name. Typical questions you can ask include:
  - "Tell me about Homer Simpson."
  - "Who are the members of the Flanders family?"
  - "Give me the portrait of Mr. Burns."

It demonstrates the following:

- How to create and configure a stdio MCP server.
- How to create and register MCP tools.

## Prerequisites

To compile and run this demo, you'll need:

- Java 25
- Maven
- Docker _(optional, used for running the application in a container)_

## Running the Application

To run the application manually, configure an MCP client to connect to the stdio MCP server:

```json
"mcp-server-stdio": {
    "type": "stdio",
    "command": "java",
    "args": [
      "-jar",
      "/path/to/executable/mcp-server-stdio-1.0.0.jar"
    ]
}
```

Alternatively, to run it using Docker, use the following configuration:

```json
"mcp-server-stdio": {
    "type": "stdio",
    "command": "docker",
    "args": [
      "run",
      "-i",
      "--rm",
      "loicgreffier/spring-boot-ai-quickstarts:mcp-server-stdio-1.0.0"
    ]
}
```