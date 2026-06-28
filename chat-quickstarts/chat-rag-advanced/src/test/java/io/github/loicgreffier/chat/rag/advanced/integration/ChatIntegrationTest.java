/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.github.loicgreffier.chat.rag.advanced.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.loicgreffier.chat.rag.advanced.controller.ChatController.Word;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ChatIntegrationTest.class);

    @Container
    @ServiceConnection
    static OllamaContainer ollama = createOllamaContainer();

    @Container
    @ServiceConnection
    static PostgreSQLContainer pgvector = new PostgreSQLContainer("pgvector/pgvector:pg18");

    @LocalServerPort
    private int port;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        webTestClient = WebTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .responseTimeout(Duration.ofMinutes(2))
                .build();
    }

    @Test
    void shouldAnswerQuestionFromRetrievedEpisode() {
        List<Word> response = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat")
                        .queryParam("userInput", "In which episodes does Homer go to space?")
                        .build())
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Word.class)
                .getResponseBody()
                .collectList()
                .block();

        assertNotNull(response);

        String answer = response.stream().map(Word::value).collect(Collectors.joining());
        log.info("LLM response: {}", answer);

        assertTrue(answer.toLowerCase().contains("deep space homer"));
    }

    /**
     * Create the Ollama container, trusting a corporate CA when {@code CORPORATE_CA_PATH} is set.
     *
     * @return the configured Ollama container
     */
    static OllamaContainer createOllamaContainer() {
        OllamaContainer container = new OllamaContainer(DockerImageName.parse("ollama/ollama:0.30.10"));

        String corporateCaPath = System.getenv("CORPORATE_CA_PATH");
        if (corporateCaPath != null && !corporateCaPath.isBlank()) {
            container
                    .withCopyFileToContainer(
                            MountableFile.forHostPath(corporateCaPath),
                            "/usr/local/share/ca-certificates/corporate-ca.crt")
                    .withCreateContainerCmdModifier(cmd -> cmd.withEntrypoint("/bin/sh"))
                    .withCommand("-c", "update-ca-certificates && exec /bin/ollama serve");
        }

        return container;
    }
}
