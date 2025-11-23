# Chat Memory JDBC

This module demonstrates how to create a streaming chatbot application that maintains chat context in a JDBC (PostgreSQL) database for up to 30 messages.
It covers the following:

- How to use the `ChatClient` API to interact with an LLM and stream responses.
- How to add JDBC (PostgreSQL) chat memory to maintain context across messages.

It leverages the following AI technology:

- Chat model: `gemma3:4b`

## Prerequisites

To compile and run this demo, you'll need:

- Java 25
- Maven
- Node.js 24
- NPM
- Ollama
- Docker _(optional, used for running the application in a container)_

## Running the Application

To run the application manually:

- Start Ollama.
- Start a PostgreSQL database in Docker using:

```bash
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=changeme -p 5432:5432 postgres:18
```

- Start the back-end server.
- Start the front-end UI using `ng serve` from the `ui` directory.

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
- PostgreSQL database
- Chat Memory JDBC application (available at http://localhost:8080)
