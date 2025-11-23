# Chat

This module demonstrates how to create a simple chatbot application.

It demonstrates the following:

- How to use the `ChatClient` API to interact synchronously with an LLM.

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
- Chat application (available at http://localhost:8080)
