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
package io.github.loicgreffier.mcp.server.stdio.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.loicgreffier.mcp.server.stdio.model.Character;
import io.github.loicgreffier.mcp.server.stdio.model.Phrase;
import io.github.loicgreffier.mcp.server.stdio.repository.CharacterRepository;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {
    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterService characterService;

    @Test
    void shouldGetCharacterByName() throws JsonProcessingException {
        when(characterRepository.findByNameContainingIgnoreCase("Simpson"))
                .thenReturn(List.of(
                        Character.builder()
                                .id(1L)
                                .name("Homer Simpson")
                                .age(39)
                                .birthDate(Date.valueOf("1956-05-12"))
                                .gender("Male")
                                .occupation("Safety Inspector")
                                .portraitUrl("https://cdn.thesimpsonsapi.com/500/character/1.webp")
                                .status("Alive")
                                .phrases(List.of(
                                        Phrase.builder().id(1L).text("Doh!").build(),
                                        Phrase.builder()
                                                .id(2L)
                                                .text("Why you little...!")
                                                .build(),
                                        Phrase.builder().id(3L).text("Woo-hoo!").build(),
                                        Phrase.builder()
                                                .id(4L)
                                                .text("Mmm... (food)... *drooling*")
                                                .build(),
                                        Phrase.builder()
                                                .id(5L)
                                                .text("Stupid Flanders!")
                                                .build(),
                                        Phrase.builder()
                                                .id(6L)
                                                .text("Shut up Flanders!")
                                                .build()))
                                .build(),
                        Character.builder()
                                .id(2L)
                                .name("Marge Simpson")
                                .age(39)
                                .gender("Female")
                                .occupation("Unemployed")
                                .portraitUrl("https://cdn.thesimpsonsapi.com/500/character/2.webp")
                                .status("Alive")
                                .phrases(List.of(
                                        Phrase.builder().id(7L).text("Hrmmm...").build(),
                                        Phrase.builder()
                                                .id(8L)
                                                .text("Now its Marges time to shine!")
                                                .build(),
                                        Phrase.builder().id(9L).text("Oh!").build(),
                                        Phrase.builder()
                                                .id(10L)
                                                .text("Oh, Homie!")
                                                .build(),
                                        Phrase.builder()
                                                .id(11L)
                                                .text("Its true. Women arent very good drivers.")
                                                .build()))
                                .build()));

        String result = characterService.getCharactersByName("Simpson");

        assertEquals(
                "[{\"id\":1,\"age\":39,\"birthDate\":\"1956-05-11\",\"gender\":\"Male\",\"name\":\"Homer Simpson\",\"occupation\":\"Safety Inspector\",\"portraitUrl\":\"https://cdn.thesimpsonsapi.com/500/character/1.webp\",\"status\":\"Alive\",\"phrases\":[{\"id\":1,\"text\":\"Doh!\"},{\"id\":2,\"text\":\"Why you little...!\"},{\"id\":3,\"text\":\"Woo-hoo!\"},{\"id\":4,\"text\":\"Mmm... (food)... *drooling*\"},{\"id\":5,\"text\":\"Stupid Flanders!\"},{\"id\":6,\"text\":\"Shut up Flanders!\"}]},{\"id\":2,\"age\":39,\"birthDate\":null,\"gender\":\"Female\",\"name\":\"Marge Simpson\",\"occupation\":\"Unemployed\",\"portraitUrl\":\"https://cdn.thesimpsonsapi.com/500/character/2.webp\",\"status\":\"Alive\",\"phrases\":[{\"id\":7,\"text\":\"Hrmmm...\"},{\"id\":8,\"text\":\"Now its Marges time to shine!\"},{\"id\":9,\"text\":\"Oh!\"},{\"id\":10,\"text\":\"Oh, Homie!\"},{\"id\":11,\"text\":\"Its true. Women arent very good drivers.\"}]}]",
                result);
    }
}
