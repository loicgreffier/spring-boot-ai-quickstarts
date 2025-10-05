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
package io.github.loicgreffier.chat.retrieval.augmented.generation.controller;

import jakarta.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.template.st.StTemplateRenderer;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private static final String ENHANCED_PROMPT_TEMPLATE =
            """
        <query>

        Context information is below.

        ---------------------
        <question_answer_context>
        ---------------------

        Given the context information, answer the query.

        Follow these rules:

        1. If the query is like "Hi", "Hello", "How are you?", "What is your name?", "Who are you?" or similar, answer with a short and friendly sentence.
        2. If the answer is not in the context, answer with your best knowledge.
        3. Avoid statements like "Based on the context..." or "The provided information...".
        """;

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    /**
     * Constructor.
     *
     * @param chatClientBuilder The chat client builder
     * @param vectorStore The vector store
     */
    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = chatClientBuilder.build();
    }

    @PostConstruct
    public void init() {
        List<Document> documents = List.of(
                new Document("Clement Pulby is the GOAT of our generation", Map.of("meta1", "meta1")),
                new Document("Clement Pulby is the brother of McFly from the duo McFly & Carlito"));

        vectorStore.add(documents);
    }

    /**
     * Chat endpoint. Each response word is wrapped in a Word object to preserve leading spaces.
     *
     * @param userInput The user input
     * @return The chat response as a stream
     */
    @GetMapping
    public Flux<Word> chat(@RequestParam boolean enhancedPromptTemplate, @RequestParam String userInput) {
        QuestionAnswerAdvisor defaultAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .searchRequest(
                        SearchRequest.builder().similarityThreshold(0.0).topK(4).build())
                .build();

        QuestionAnswerAdvisor enhancedAdvisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(PromptTemplate.builder()
                        .renderer(StTemplateRenderer.builder()
                                .startDelimiterToken('<')
                                .endDelimiterToken('>')
                                .build())
                        .template(ENHANCED_PROMPT_TEMPLATE)
                        .build())
                .searchRequest(
                        SearchRequest.builder().similarityThreshold(0.0).topK(4).build())
                .build();

        Flux<String> chatResponse =
                chatClient
                        .prompt()
                        .advisors(enhancedPromptTemplate ? enhancedAdvisor : defaultAdvisor)
                        .user(userInput)
                        .stream()
                        .content();

        return chatResponse.map(Word::new);
    }

    /** A word in the chat response. */
    @Data
    public static class Word {
        private String value;

        public Word(String value) {
            this.value = value;
        }
    }
}
