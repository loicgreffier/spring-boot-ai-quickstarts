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
package io.github.loicgreffier.chat.rag.controller;

import com.opencsv.exceptions.CsvValidationException;
import io.github.loicgreffier.chat.rag.data.RagData;
import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    /** Custom prompt template for the question-answering advisor. */
    private static final String PROMPT_TEMPLATE = """
        You are a helpful assistant who answers questions about episodes of The Simpsons TV show.

        Use only the episode information in the context below to answer. Each entry has a name and synopsis.

        <context>
        {question_answer_context}
        </context>

        Answer the user's query following these rules:

        1. Use only the context. Never invent episode names, plots, or details that are not present in it.
        2. When relevant, reference the episode by its name.
        3. Do not mention the context itself. Avoid phrases like "Based on the context" or "The provided information".
        4. If the context does not contain the answer, say your knowledge base doesn't cover it and invite the user to rephrase.
        5. If the query is a greeting, farewell, or small talk (e.g. "Hello", "Thanks", "Goodbye"), respond politely and naturally.
        6. Keep answers concise.

        <query>
        {query}
        </query>
        """;

    private static final Double SIMILARITY_SEARCH = 0.35;
    private static final Integer TOP_K = 10;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    /**
     * Constructor. Creates a chat client with a question-answering advisor using the provided vector store and a custom
     * prompt template.
     *
     * @param chatClientBuilder The chat client builder
     * @param vectorStore The vector store
     */
    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        QuestionAnswerAdvisor questionAnswerAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .order(0)
                .promptTemplate(
                        PromptTemplate.builder().template(PROMPT_TEMPLATE).build())
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(SIMILARITY_SEARCH)
                        .topK(TOP_K)
                        .build())
                .build();

        SimpleLoggerAdvisor loggerAdvisor =
                SimpleLoggerAdvisor.builder().order(1).build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(questionAnswerAdvisor, loggerAdvisor)
                .build();
        this.vectorStore = vectorStore;
    }

    /**
     * Pre-populate the vector store with episodes once the application is ready.
     *
     * @throws CsvValidationException If the CSV file is invalid
     * @throws IOException If the CSV file cannot be read
     */
    @EventListener(ApplicationReadyEvent.class)
    public void loadEpisodes() throws CsvValidationException, IOException {
        long startTime = System.currentTimeMillis();
        List<Document> episodes = RagData.loadEpisodes();
        vectorStore.add(episodes);
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        log.info("Loaded {} episodes into the vector store in {}s", episodes.size(), duration);
    }

    /**
     * Chat endpoint. Each response word is wrapped in a Word object to preserve leading spaces.
     *
     * @param userInput The user input
     * @return The chat response as a stream
     */
    @GetMapping("/chat")
    public Flux<Word> chat(@RequestParam String userInput) {
        Flux<String> chatResponse = chatClient.prompt().user(userInput).stream().content();

        return chatResponse.map(Word::new);
    }

    /**
     * A word in the chat response.
     *
     * @param value The word value
     */
    public record Word(String value) {}
}
