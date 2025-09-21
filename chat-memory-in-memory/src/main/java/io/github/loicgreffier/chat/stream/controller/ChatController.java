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
package io.github.loicgreffier.chat.stream.controller;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

import java.util.List;
import lombok.Data;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
public class ChatController {
    private final ChatClient chatClient;
    private final ChatMemory chatMemory;

    /**
     * Constructor.
     *
     * @param chatClientBuilder The chat client builder
     */
    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(new InMemoryChatMemoryRepository())
                .maxMessages(30)
                .build();

        this.chatClient = chatClientBuilder
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    /**
     * Chat endpoint. Each response word is wrapped in a Word object to preserve leading spaces.
     *
     * @param conversationId The conversation ID
     * @param userInput The user input
     * @return The chat response as a stream
     */
    @GetMapping("/{conversationId}")
    public Flux<Word> chat(@PathVariable String conversationId, @RequestParam String userInput) {
        Flux<String> chatResponse = chatClient
                .prompt()
                .advisors(advisorSpec -> advisorSpec.param(CONVERSATION_ID, conversationId))
                .user(userInput)
                .stream()
                .content();

        return chatResponse.map(Word::new);
    }

    /**
     * Get the conversation history.
     *
     * @param conversationId The conversation ID
     * @return The conversation history
     */
    @GetMapping("/{conversationId}/history")
    public List<Message> getConversationHistory(@PathVariable String conversationId) {
        return chatMemory.get(conversationId);
    }

    /**
     * Delete a conversation.
     *
     * @param conversationId The conversation ID
     */
    @DeleteMapping("/{conversationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteConversation(@PathVariable String conversationId) {
        chatMemory.clear(conversationId);
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
