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
package io.github.loicgreffier.chat.rag.advanced.controller;

import io.github.loicgreffier.chat.rag.advanced.data.RagData;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.expansion.MultiQueryExpander;
import org.springframework.ai.rag.preretrieval.query.transformation.TranslationQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping
public class ChatController {
    /** Custom prompt template for the question-answering advisor. */
    private static final String PROMPT_TEMPLATE = """
        You are a helpful assistant who answers questions about episodes of The Simpsons TV show. Here is the user query surrounded by ---------------------

        ---------------------
        {query}
        ---------------------

        Context information is below, surrounded by ---------------------

        ---------------------
        {context}
        ---------------------

        Given the context, respond to the user's query following the rules below:

        1. Do not use prior knowledge, only the given context.
        2. Do not mention the context. Avoid phrases like "Based on the context" or "The provided information".
        3. If the context does not provide enough information, the user's query is outside your knowledge base. Politely inform the user that your knowledge base doesn't contain the answer, and suggest that they clarify their question.
        4. Respond in the same language as the user's query.
        5. If the user query is a general greeting, farewell, or small talk (e.g., "Hello," "How are you," "Goodbye," "Thanks," etc.), respond politely and naturally.
        """;

    /** Custom prompt template for the question-answering advisor. */
    private static final String EMPTY_PROMPT_TEMPLATE = """
        You are a helpful assistant who answers questions about the Simpsons TV show.
        The user's query is outside your knowledge base.
        Respond following the rules below:

        1. Politely inform the user that your knowledge base doesn't contain the answer, and suggest that they clarify their question.
        """;

    private static final Double SIMILARITY_SEARCH = 0.5;
    private static final Integer TOP_K = 5;

    private final ChatClient chatClient;

    /**
     * Constructor. Creates a chat client with a question-answering advisor using the provided vector store and a custom
     * prompt template. The vector store is pre-populated with documents.
     *
     * @param chatClientBuilder The chat client builder
     * @param vectorStore The vector store
     */
    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        Advisor ragAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(TranslationQueryTransformer.builder()
                        .chatClientBuilder(chatClientBuilder.build().mutate())
                        .targetLanguage("english")
                        .build())
                .queryExpander(MultiQueryExpander.builder()
                        .chatClientBuilder(chatClientBuilder.build().mutate())
                        .numberOfQueries(3)
                        .includeOriginal(false)
                        .build())
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(SIMILARITY_SEARCH)
                        .topK(TOP_K)
                        .vectorStore(vectorStore)
                        .build())
                .documentPostProcessors((query, documents) -> {
                    String logDocuments = documents.stream()
                            .map(doc -> "%s (score: %s)".formatted(doc.getText(), doc.getScore()))
                            .reduce("", (a, b) -> a + "\n- " + b);

                    log.info("Retrieved {} documents for query \"{}\"{}", documents.size(), query.text(), logDocuments);
                    return documents;
                })
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .promptTemplate(PromptTemplate.builder()
                                .template(PROMPT_TEMPLATE)
                                .build())
                        .allowEmptyContext(false)
                        .emptyContextPromptTemplate(PromptTemplate.builder()
                                .template(EMPTY_PROMPT_TEMPLATE)
                                .build())
                        .build())
                .build();

        SimpleLoggerAdvisor loggerAdvisor =
                SimpleLoggerAdvisor.builder().order(1).build();

        this.chatClient =
                chatClientBuilder.defaultAdvisors(ragAdvisor, loggerAdvisor).build();

        long startTime = System.currentTimeMillis();
        vectorStore.add(RagData.loadEpisodes());
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        log.info(
                "Loaded {} episodes into the vector store in {}s",
                RagData.loadEpisodes().size(),
                duration);
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
