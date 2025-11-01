# Chat RAG Advanced

This module demonstrates how to create a streaming chatbot application with advanced retrieval-augmented generation (RAG) capabilities, using the Simpsons TV show as an example.
It covers the following:

- How to use the `ChatClient` API to interact with an LLM and stream responses.
- How to connect to a vector database, store embeddings, and perform similarity searches.
- How to tune the RAG process using the `RetrievalAugmentationAdvisor` API with advanced components:
  - A `QueryTransformer` to translate user queries in english.
  - A `QueryExpander` to multiply user queries into multiple related queries.
  - A `DocumentPostProcessor` to log retrieved documents with similarity scores.
  - A custom prompt template—used, for example, to ensure responses are in the same language as the user’s query—and a custom empty-context prompt.
- How to use the `SimpleLoggerAdvisor` to log the final prompt and response.

It leverages the following AI technologies:

- Chat model: `gemma3:4b`
- Embedding model: `qwen3-embedding:0.6b`
- Vector database: PGVector

<img src=".readme/demo.gif" alt="Spring AI demo"/>

## Prerequisites


To compile and run this demo, you’ll need:

- Java 21
- Maven
- Node.js 24
- NPM
- Ollama
- Docker

## Running the Application

To run the application manually:

- Start Ollama.
- Start a PGVector database in Docker using:

```bash
docker run -d -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=changeme -p 5432:5432 pgvector/pgvector:pg18
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
- PGVector database
- Chat RAG Advanced application (available at http://localhost:8080)
