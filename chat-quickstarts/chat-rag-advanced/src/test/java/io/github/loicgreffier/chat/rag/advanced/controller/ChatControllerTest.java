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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import io.github.loicgreffier.chat.rag.advanced.controller.ChatController.Word;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.vectorstore.VectorStore;
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
    private VectorStore vectorStore;

    @Test
    void shouldAnswerQuestion() {
        when(chatClientBuilder.build()).thenReturn(chatClient);
        // Used while building the query transformer and the query expander in the controller constructor.
        lenient().when(chatClient.mutate()).thenReturn(chatClientBuilder);
        when(chatClientBuilder.defaultAdvisors(any(Advisor.class), any(Advisor.class)))
                .thenReturn(chatClientBuilder);
        when(chatClient.prompt()).thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.user("In which episodes does Homer go to space?"))
                .thenReturn(chatClientRequestSpec);
        when(chatClientRequestSpec.stream()).thenReturn(streamResponseSpec);
        when(streamResponseSpec.content()).thenReturn(Flux.just("Deep Space ", "Homer"));

        ChatController chatController = new ChatController(chatClientBuilder, vectorStore);
        List<Word> response = chatController
                .chat("In which episodes does Homer go to space?")
                .collectList()
                .block();

        assertNotNull(response);
        assertEquals(List.of(new Word("Deep Space "), new Word("Homer")), response);
    }
}
