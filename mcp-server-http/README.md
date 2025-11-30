# MCP Server HTTP

This module demonstrates how to create an MCP server using streamable HTTP communication.
It uses data from [The Simpsons Episode API](https://thesimpsonsapi.com), which is pre-fetched in `episodes.csv` file and stored in a PGvector database when the application starts.

It exposes the following tools:
- `get_episodes_by_description`, which returns information about Simpsons episodes using semantic search based on a description. Typical questions you can ask include:
  - "In which episodes does Homer go to space?"
  - "In which episodes does Sideshow Bob try to kill Bart?"
  - "Which episodes are themed around Halloween?"

It demonstrates the following:

- How to create and configure a streamable HTTP MCP server.
- How to create and register MCP tools.

It leverages the following AI technologies:
- Embedding model: `qwen3-embedding:0.6b`
- Vector database: PGvector

## Prerequisites

To compile and run this demo, you'll need:

- Java 25
- Maven
- Ollama
- Docker

## Running the Application

To run the application manually:

- Start Ollama.
- Start a PGvector database in Docker using:

```bash
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=changeme -p 5432:5432 pgvector/pgvector:pg18
```

- Start the back-end server.
- Configure an MCP client to connect to the streamable HTTP MCP server:

```json
"mcp-server-http": {
    "type": "http",
    "url": "https://localhost:8080/mcp"
}
```

Alternatively, to run everything at once using Docker (CPU only), run:

```bash
docker-compose up -d
```

To run it with [NVIDIA GPU support pre-configured](https://hub.docker.com/r/ollama/ollama#nvidia-gpu), run:

```bash
docker-compose -f docker-compose-nvidia.yml up -d
```

This will start the following services in Docker:

- Ollama
- PGvector database
- MCP Server HTTP (available at http://localhost:8080/mcp)