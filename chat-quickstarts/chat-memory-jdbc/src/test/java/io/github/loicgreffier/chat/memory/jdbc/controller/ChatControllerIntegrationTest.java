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
package io.github.loicgreffier.chat.memory.jdbc.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.github.loicgreffier.chat.memory.jdbc.controller.ChatController.Word;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ollama.OllamaContainer;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(ChatControllerIntegrationTest.class);

    @Container
    @ServiceConnection
    static OllamaContainer ollama = createOllamaContainer();

    @Container
    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18");

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
    void shouldRememberContextAcrossQuestions() {
        List<Word> firstResponse = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/{conversationId}")
                        .queryParam("userInput", "Hello, my name is Loïc and I work as a software engineer.")
                        .build("test-conversation"))
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Word.class)
                .getResponseBody()
                .collectList()
                .block();

        assertNotNull(firstResponse);

        String firstAnswer = firstResponse.stream().map(Word::value).collect(Collectors.joining());
        log.info("LLM response: {}", firstAnswer);

        List<Word> response = webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path("/chat/{conversationId}")
                        .queryParam("userInput", "Do you know my name?")
                        .build("test-conversation"))
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

        assertTrue(answer.toLowerCase().contains("loïc"));

        List<Map<String, Object>> history = webTestClient
                .get()
                .uri("/chat/{conversationId}/history", "test-conversation")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(history);
        assertEquals(4, history.size());
        assertEquals(
                "Hello, my name is Loïc and I work as a software engineer.",
                history.get(0).get("text"));
        assertEquals("Do you know my name?", history.get(2).get("text"));

        webTestClient
                .delete()
                .uri("/chat/{conversationId}", "test-conversation")
                .exchange()
                .expectStatus()
                .isNoContent();

        List<Map<String, Object>> emptyHistory = webTestClient
                .get()
                .uri("/chat/{conversationId}/history", "test-conversation")
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                .returnResult()
                .getResponseBody();

        assertNotNull(emptyHistory);
        assertEquals(0, emptyHistory.size());
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
