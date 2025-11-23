# MCP Server Stdio

This module demonstrates how to create a MCP server with standard I/O (stdio) communication.
It covers the following:

- How to create and configure a stdio MCP server.
- How to create a MCP tool that performs a simple database research and returns the result via stdio.

## Prerequisites

To compile and run this demo, you'll need:

- Java 25
- Maven
- Docker _(optional, used for running the application in a container)_

## Running the Application

To run the application manually:

- After building the project with Maven, configure a MCP client to connect to the stdio MCP server.

```json
"simpsons-mcp-server": {
    "type": "stdio",
    "command": "java",
    "args": [
      "-jar",
      "mcp-server-stdio-1.0.0.jar"
    ]
}
```

