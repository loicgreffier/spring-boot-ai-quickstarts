# Chat Stream

This module demonstrates how to create a streaming chatbot application.
It covers the following:

- How to use the `ChatClient` API to interact with an LLM model and stream responses.

It leverages the following AI technology:

- Chat model: `gemma3:4b`

<img src=".readme/demo.gif" alt="Spring AI demo"/>

## Prerequisites

To compile and run this demo, you’ll need:

- Java 21
- Maven
- Node.js 24
- NPM
- Ollama
- Docker _(optional, used for running the applications in containers)_

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
- Chat Stream application (available at http://localhost:8080)
