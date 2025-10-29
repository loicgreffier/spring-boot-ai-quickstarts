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

import static io.github.loicgreffier.chat.rag.data.RagData.DOCUMENTS;

import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
public class ChatController {
    /** Custom prompt template for the question-answering advisor. */
    private static final String PROMPT_TEMPLATE =
            """
        {query}

        Context information is below, surrounded by ---------------------

        ---------------------
        {question_answer_context}
        ---------------------

        Given the context, respond to the user's query following the rules below:

        1. Do not use prior knowledge, only the given context.
        2. If the answer can't be found in the context, or if the context is empty, politely inform the user that your knowledge base doesn't contain the answer, and suggest that they clarify their question.
        3. If the user query is a general greeting, farewell, or small talk (e.g., "Hello," "How are you," "Goodbye," "Thanks," etc.), respond politely and naturally.
        """;

    private static final Double SIMILARITY_SEARCH = 0.5;
    private static final Integer TOP_K = 6;

    private final ChatClient chatClient;

    /**
     * Constructor. Creates a chat client with a question-answering advisor using the provided vector store and a custom
     * prompt template. The vector store is pre-populated with documents.
     *
     * @param chatClientBuilder The chat client builder
     * @param vectorStore The vector store
     */
    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(
                        PromptTemplate.builder().template(PROMPT_TEMPLATE).build())
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(SIMILARITY_SEARCH)
                        .topK(TOP_K)
                        .build())
                .build();

        this.chatClient = chatClientBuilder.defaultAdvisors(advisor).build();

        vectorStore.add(DOCUMENTS);
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

    /** A word in the chat response. */
    @Data
    public static class Word {
        private String value;

        public Word(String value) {
            this.value = value;
        }
    }
}
