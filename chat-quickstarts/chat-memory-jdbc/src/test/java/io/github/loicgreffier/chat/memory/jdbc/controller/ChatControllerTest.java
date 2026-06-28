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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.github.loicgreffier.chat.memory.jdbc.controller.ChatController.Word;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.memory.repository.jdbc.JdbcChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
class ChatControllerTest {
    @Mock
    private ChatClient.Builder chatClientBuilder;

    @Mock
    private ChatClient chatClient;

    @Mock
    private ChatClient.ChatClientRequestSpec chatClientRequestSpec;

    @Mock
    private ChatClient.StreamResponseSpec streamResponseSpec;

    @Mock
    private JdbcChatMemoryRepository chatMemoryRepository;

    private ChatController chatController;

    @BeforeEach
    void setUp() {
        when(chatClientBuilder.defaultAdvisors(any(Advisor.class))).thenReturn(chatClientBuilder);
        when(chatClientBuilder.build()).thenReturn(chatClient);
        chatController = new ChatController(chatClientBuilder, chatMemoryRepository);
    }

    @Test
    @SuppressWarnings("unchecked")
    void shouldAnswerUserInput() {
        when(chatClient.prompt()).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.advisors(any(Consumer.class))).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.user("Who voices Homer Simpson?")).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.stream()).thenReturn(streamResponseSpec);
        when(streamResponseSpec.content()).thenReturn(Flux.just("Homer Simpson is voiced ", "by Dan Castellaneta."));

        List<Word> response = chatController
                .chat("conversation-id", "Who voices Homer Simpson?")
                .collectList()
                .block();

        assertNotNull(response);
        assertEquals(List.of(new Word("Homer Simpson is voiced "), new Word("by Dan Castellaneta.")), response);
    }

    @Test
    void shouldGetConversationHistory() {
        UserMessage userMessage = new UserMessage("Who voices Homer Simpson?");
        AssistantMessage assistantMessage = new AssistantMessage("Homer Simpson is voiced by Dan Castellaneta.");
        when(chatMemoryRepository.findByConversationId("conversation-id"))
                .thenReturn(List.of(userMessage, assistantMessage));

        List<Message> history = chatController.getConversationHistory("conversation-id");

        assertNotNull(history);
        assertEquals(2, history.size());
        assertEquals("Who voices Homer Simpson?", history.get(0).getText());
        assertEquals(
                "Homer Simpson is voiced by Dan Castellaneta.", history.get(1).getText());
    }

    @Test
    void shouldDeleteConversation() {
        chatController.deleteConversation("conversation-id");

        verify(chatMemoryRepository).deleteByConversationId("conversation-id");
    }
}
