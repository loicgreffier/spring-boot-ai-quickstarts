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

import io.github.loicgreffier.mcp.server.stdio.model.Episode;
import io.github.loicgreffier.mcp.server.stdio.repository.EpisodeRepository;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import tools.jackson.core.JacksonException;

@ExtendWith(MockitoExtension.class)
class EpisodeServiceTest {
    @Mock
    private EpisodeRepository episodeRepository;

    @InjectMocks
    private EpisodeService episodeService;

    @Test
    void shouldGetEpisodes() throws JacksonException {
        when(episodeRepository.searchByTerm("Simpson"))
                .thenReturn(List.of(
                        Episode.builder()
                                .id(1L)
                                .airdate(Date.from(Instant.parse("1989-12-17T00:00:00Z")))
                                .episodeNumber(1)
                                .imagePath("https://cdn.thesimpsonsapi.com/episode/1.webp")
                                .name("Simpsons Roasting on an Open Fire")
                                .season(1)
                                .synopsis(
                                        "When Mr. Burns announces that none of the workers will be getting Christmas bonuses and Marge reveals that she spent the extra Christmas gift money on getting Bart''s \"Mother\" tattoo removed, Homer keeps his lack of funds for the holidays a secret and gets a job as a mall Santa.")
                                .build(),
                        Episode.builder()
                                .id(2L)
                                .episodeNumber(2)
                                .imagePath("https://cdn.thesimpsonsapi.com/episode/2.webp")
                                .name("Bart the Genius")
                                .season(1)
                                .synopsis(
                                        "To get back at class nerd/teacher''s pet, Martin Prince, for ratting him out to Principal Skinner for vandalism on school property, Bart switches his own intelligence test with Martin''s. During a parent/principal conference about the defaced wall, the school counselor, Dr. Pryor, announces that Bart is a genius and only acts out because public school isn''t stimulating enough, so Bart is sent to a school for genius kids — and finds out just how painfully below average he is.")
                                .build()));

        String result = episodeService.getEpisodes("Simpson");

        assertEquals(
                "[{\"id\":1,\"airdate\":\"1989-12-17\",\"episodeNumber\":1,\"imagePath\":\"https://cdn.thesimpsonsapi.com/episode/1.webp\",\"name\":\"Simpsons Roasting on an Open Fire\",\"season\":1,\"synopsis\":\"When Mr. Burns announces that none of the workers will be getting Christmas bonuses and Marge reveals that she spent the extra Christmas gift money on getting Bart''s \\\"Mother\\\" tattoo removed, Homer keeps his lack of funds for the holidays a secret and gets a job as a mall Santa.\"},{\"id\":2,\"airdate\":null,\"episodeNumber\":2,\"imagePath\":\"https://cdn.thesimpsonsapi.com/episode/2.webp\",\"name\":\"Bart the Genius\",\"season\":1,\"synopsis\":\"To get back at class nerd/teacher''s pet, Martin Prince, for ratting him out to Principal Skinner for vandalism on school property, Bart switches his own intelligence test with Martin''s. During a parent/principal conference about the defaced wall, the school counselor, Dr. Pryor, announces that Bart is a genius and only acts out because public school isn''t stimulating enough, so Bart is sent to a school for genius kids — and finds out just how painfully below average he is.\"}]",
                result);
    }
}
