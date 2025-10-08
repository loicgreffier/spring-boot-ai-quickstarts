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
    private static final String CUSTOM_PROMPT_TEMPLATE =
            """
        {query}

        Context information is below, surrounded by ---------------------

        ---------------------
        {question_answer_context}
        ---------------------
        
        Answer the query following these rules:

        1. If the query is ONLY a greeting (like "Hi", "Hello", "Hey") with no other question or topic:
           - Respond with a brief greeting and ignore the context completely even if it contains relevant information
        2. If the context contains relevant information to answer the query:
            - Answer using ONLY the information from the context
            - Do not add information from your general knowledge
        3. If the context does NOT contain relevant information to answer the query:
            - Answer using your general knowledge
            - Provide the best answer you can
        4. CRITICAL: Never acknowledge, reference, or mention the context in ANY way. Forbidden phrases include:
           - "based on the context/information/document"
           - "according to the context/provided information"
           - "the context shows/states/mentions/doesn't mention"
           - "in the given/provided context"
           - "the information provided"
           - "isn't mentioned in the context"
           - "the context doesn't contain"
           - Or ANY similar reference to source materials
        5. Always answer as if the knowledge comes directly from you, naturally and conversationally.
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
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;

        List<Document> documents = List.of(
                new Document("The Simpsons is an animated sitcom created by Matt Groening that first aired in 1989."),
                new Document("Homer Simpson works as a safety inspector at the Springfield Nuclear Power Plant."),
                new Document("Marge Simpson is the matriarch of the family, known for her tall blue beehive hairdo."),
                new Document("Bart Simpson is the eldest child, a ten-year-old troublemaker who attends Springfield Elementary School."),
                new Document("Lisa Simpson is an eight-year-old intellectual who plays the saxophone and advocates for various causes."),
                new Document("Maggie Simpson is the baby of the family who communicates by sucking on her pacifier."),
                new Document("The family lives at 742 Evergreen Terrace in the fictional town of Springfield."),
                new Document("Mr. Burns is Homer's boss and the wealthy, evil owner of the nuclear power plant."),
                new Document("Waylon Smithers is Mr. Burns' devoted assistant and the plant's executive."),
                new Document("Ned Flanders is the Simpsons' religious, cheerful next-door neighbor."),
                new Document("Moe Szyslak runs Moe's Tavern, where Homer and his friends frequently drink."),
                new Document("Apu Nahasapeemapetilon operates the Kwik-E-Mart convenience store."),
                new Document("Chief Wiggum is Springfield's incompetent police chief."),
                new Document("Krusty the Clown hosts a children's television show that Bart idolizes."),
                new Document("Sideshow Bob is Krusty's former sidekick who repeatedly tries to kill Bart."),
                new Document("Principal Skinner runs Springfield Elementary School alongside Superintendent Chalmers."),
                new Document("The show is known for its satirical take on American culture, society, and politics."),
                new Document("Each episode typically opens with the family gathering on their couch in different ways."),
                new Document("The Simpsons holds the record as the longest-running American animated series."),
                new Document("The show features hundreds of recurring characters that make up the town of Springfield.")
        );

        vectorStore.add(documents);
    }

    @PostConstruct
    public void init() {

    }

    /**
     * Chat endpoint. Each response word is wrapped in a Word object to preserve leading spaces.
     *
     * @param userInput The user input
     * @return The chat response as a stream
     */
    @GetMapping
    public Flux<Word> chat(
            @RequestParam(defaultValue = "false") boolean customPromptTemplate, @RequestParam String userInput) {
        QuestionAnswerAdvisor advisor = customPromptTemplate
                ? QuestionAnswerAdvisor.builder(vectorStore)
                        .promptTemplate(PromptTemplate.builder()
                                .template(CUSTOM_PROMPT_TEMPLATE)
                                .build())
                        .searchRequest(SearchRequest.builder()
                                .similarityThreshold(0.6)
                                .topK(3)
                                .build())
                        .build()
                : new QuestionAnswerAdvisor(vectorStore);

        Flux<String> chatResponse = chatClient
                .prompt()
                .user(userInput)
                .advisors(advisor)
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
