# MCP Server Stdio

This module demonstrates how to create an MCP server using standard I/O (stdio) communication.
It covers the following:

- How to create and configure a stdio MCP server.
- How to create and register MCP tools.

This demo exposes a tool, `get_characters_by_name`, that returns information about Simpsons characters based on a given name.
The data was retrieved from [The Simpsons API](https://thesimpsonsapi.com) and stored in an in-memory H2 database when the application starts.

## Prerequisites

To compile and run this demo, you'll need:

- Java 25
- Maven
- Docker _(optional, used for running the application in a container)_

## Running the Application

To run the application manually:

- After building the project with Maven, configure an MCP client to connect to the stdio MCP server:

```json
"simpsons-mcp-server": {
    "type": "stdio",
    "command": "java",
    "args": [
      "-jar",
      "/path/to/executable/mcp-server-stdio-1.0.0.jar"
    ]
}
```