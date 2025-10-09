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

import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {
    /**
     * Custom prompt template for the question-answering advisor.
     */
    private static final String PROMPT_TEMPLATE =
            """
        {query}

        Context information is below, surrounded by ---------------------

        ---------------------
        {question_answer_context}
        ---------------------

        Given the context and the provided history information but not prior knowledge,
        respond to the user's query. If the answer is not found in the context, inform
        the user that your knowledge base does not contain the answer and suggest that
        they rephrase their question or ask it in a different way. You can respond to
        general chat questions such as "Hello", "How are you?", "How do you work?" or "How can you help me?".
        """;

    private final ChatClient chatClient;

    /**
     * Constructor.
     * Creates a chat client with a question-answering advisor using the provided vector store and a custom prompt template.
     * The vector store is pre-populated with documents.
     *
     * @param chatClientBuilder The chat client builder
     * @param vectorStore The vector store
     */
    public ChatController(ChatClient.Builder chatClientBuilder, VectorStore vectorStore) {
        QuestionAnswerAdvisor advisor = QuestionAnswerAdvisor.builder(vectorStore)
                .promptTemplate(PromptTemplate.builder()
                        .template(PROMPT_TEMPLATE)
                        .build())
                .searchRequest(SearchRequest.builder()
                        .similarityThreshold(0.4)
                        .topK(6)
                        .build())
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(advisor)
                .build();

        List<Document> documents = List.of(
                new Document(
                        UUID.nameUUIDFromBytes("SIM-001".getBytes()).toString(),
                        "The Simpsons is an animated sitcom created by Matt Groening that first aired in 1989.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("HOM-001".getBytes()).toString(),
                        "Homer Simpson works as a safety inspector at the Springfield Nuclear Power Plant.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("MAR-001".getBytes()).toString(),
                        "Marge Simpson is the matriarch of the family, known for her tall blue beehive hairdo.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("BAR-001".getBytes()).toString(),
                        "Bart Simpson is the eldest child, a ten-year-old troublemaker who attends Springfield Elementary School.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("LIS-001".getBytes()).toString(),
                        "Lisa Simpson is an eight-year-old intellectual who plays the saxophone and advocates for various causes.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("MAG-001".getBytes()).toString(),
                        "Maggie Simpson is the baby of the family who communicates by sucking on her pacifier.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("FAM-001".getBytes()).toString(),
                        "The family lives at 742 Evergreen Terrace in the fictional town of Springfield.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("BUR-001".getBytes()).toString(),
                        "Mr. Burns is Homer's boss and the wealthy, evil owner of the nuclear power plant.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("SMI-001".getBytes()).toString(),
                        "Waylon Smithers is Mr. Burns' devoted assistant and the plant's executive.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("FLA-001".getBytes()).toString(),
                        "Ned Flanders is the Simpsons' religious, cheerful next-door neighbor.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("MOE-001".getBytes()).toString(),
                        "Moe Szyslak runs Moe's Tavern, where Homer and his friends frequently drink.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("APU-001".getBytes()).toString(),
                        "Apu Nahasapeemapetilon operates the Kwik-E-Mart convenience store.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("WIG-001".getBytes()).toString(),
                        "Chief Wiggum is Springfield's incompetent police chief.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("KRU-001".getBytes()).toString(),
                        "Krusty the Clown hosts a children's television show that Bart idolizes.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("BOB-001".getBytes()).toString(),
                        "Sideshow Bob is Krusty's former sidekick who repeatedly tries to kill Bart.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("SKI-001".getBytes()).toString(),
                        "Principal Skinner runs Springfield Elementary School alongside Superintendent Chalmers.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("SHO-001".getBytes()).toString(),
                        "The show is known for its satirical take on American culture, society, and politics.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("COU-001".getBytes()).toString(),
                        "Each episode typically opens with the family gathering on their couch in different ways.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("REC-001".getBytes()).toString(),
                        "The Simpsons holds the record as the longest-running American animated series.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("SPR-001".getBytes()).toString(),
                        "The show features hundreds of recurring characters that make up the town of Springfield.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("EPS-001".getBytes()).toString(),
                        "The Simpsons has aired over 750 episodes across more than 35 seasons.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("LOC-001".getBytes()).toString(),
                        "Springfield's state location is deliberately kept ambiguous as a running joke throughout the series.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("TUL-001".getBytes()).toString(),
                        "The show was developed from animated shorts that appeared on The Tracey Ullman Show from 1987 to 1989.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("MUS-001".getBytes()).toString(),
                        "Danny Elfman composed The Simpsons' iconic opening theme music.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("LCT-001".getBytes()).toString(),
                        "Springfield contains numerous recurring locations including the nuclear power plant, elementary school, and Moe's Tavern.",
                        Map.of()),
                new Document(
                        UUID.nameUUIDFromBytes("MOV-001".getBytes()).toString(),
                        "The Simpsons Movie was released in theaters in 2007 after years of development.",
                        Map.of()));

        vectorStore.add(documents);
    }

    /**
     * Chat endpoint. Each response word is wrapped in a Word object to preserve leading spaces.
     *
     * @param userInput The user input
     * @return The chat response as a stream
     */
    @GetMapping
    public Flux<Word> chat(@RequestParam String userInput) {
        Flux<String> chatResponse = chatClient
                .prompt()
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
