# Chat

This module demonstrates how to create a simple chatbot application.
It demonstrates the following:

- How to use the ChatClient API to interact synchronously with an LLM model.

<img src=".readme/demo.gif" alt="Spring AI demo"/>

## Prerequisites

To compile and run this demo, you’ll need:

- Java 21
- Maven
- Node.js 24
- NPM
- Docker

## Running the Application

To run the application manually:

- Start [Ollama](https://ollama.com/).
- Start the back-end server.
- Start the front-end UI using `ng serve` from the `ui` directory.

Alternatively, to run the application with Docker, use the following command:

```console
docker-compose up -d
```

This will start the following services in Docker:

- Ollama
- Open WebUI (available at http://localhost:3000)
- Chat application (available at http://localhost:8080)
