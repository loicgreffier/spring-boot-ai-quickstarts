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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@RestController
@RequestMapping
public class ChatController {
    private static final Logger log = LoggerFactory.getLogger(ChatController.class);

    /** Custom prompt template used when relevant context was retrieved. */
    private static final String PROMPT_TEMPLATE = """
        You are a helpful assistant who answers questions about episodes of The Simpsons TV show.

        Use only the episode information in the context below to answer. Each entry has a title and synopsis.

        <context>
        {context}
        </context>

        Answer the user's query following these rules:

        1. Use only the context. Never invent episode titles, plots, or details that are not present in it.
        2. When relevant, reference the episode by its title.
        3. Do not mention the context itself. Avoid phrases like "Based on the context" or "The provided information".
        4. If the context does not contain the answer, say your knowledge base doesn't cover it and invite the user to rephrase.
        5. Always respond in the same language as the text inside <query>, regardless of the language of the context.
        6. If the query is a greeting, farewell, or small talk (e.g. "Hello", "Thanks", "Goodbye"), respond politely and naturally.
        7. Keep answers concise.

        <query>
        {query}
        </query>
        """;

    /** Prompt template used when no relevant context was retrieved. */
    private static final String EMPTY_PROMPT_TEMPLATE = """
        You are a helpful assistant who answers questions about episodes of The Simpsons TV show.
        No relevant episode information was found to answer the user's request.

        Politely let the user know that your knowledge base only covers The Simpsons episodes and that you could not
        find an answer, then invite them to rephrase or ask about a specific episode.
        """;

    private static final Double SIMILARITY_SEARCH = 0.35;
    private static final Integer TOP_K = 8;

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
                .order(0)
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
                            .map(doc -> "%s%n(Score: %s)".formatted(doc.getText(), doc.getScore()))
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

    /**
     * A word in the chat response.
     *
     * @param value The word value
     */
    public record Word(String value) {}
}
