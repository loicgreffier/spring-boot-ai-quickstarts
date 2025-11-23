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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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
    void shouldGetCharacterByName() {
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

        assertThat("""
                ID: 1
                Name: Homer Simpson
                Age: 39
                BirthDate: 1956-05-12
                Gender: Male
                Occupation: Safety Inspector
                Portrait URL: https://cdn.thesimpsonsapi.com/500/character/1.webp
                Status: Alive
                Phrases:
                - Doh!
                - Why you little...!
                - Woo-hoo!
                - Mmm... (food)... *drooling*
                - Stupid Flanders!
                - Shut up Flanders!

                ID: 2
                Name: Marge Simpson
                Age: 39
                BirthDate: null
                Gender: Female
                Occupation: Unemployed
                Portrait URL: https://cdn.thesimpsonsapi.com/500/character/2.webp
                Status: Alive
                Phrases:
                - Hrmmm...
                - Now its Marges time to shine!
                - Oh!
                - Oh, Homie!
                - Its true. Women arent very good drivers.""").isEqualToNormalizingNewlines(result);
    }
}
