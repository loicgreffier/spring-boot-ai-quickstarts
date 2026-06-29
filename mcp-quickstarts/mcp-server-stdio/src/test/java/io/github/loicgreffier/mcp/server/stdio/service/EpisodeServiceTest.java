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

import io.github.loicgreffier.mcp.server.stdio.projection.EpisodeProjection;
import io.github.loicgreffier.mcp.server.stdio.repository.EpisodeRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EpisodeServiceTest {
    @Mock
    private EpisodeRepository episodeRepository;

    @Mock
    private EpisodeProjection firstEpisode;

    @Mock
    private EpisodeProjection secondEpisode;

    @InjectMocks
    private EpisodeService episodeService;

    @Test
    void shouldGetEpisodes() {
        List<EpisodeProjection> episodes = List.of(firstEpisode, secondEpisode);
        when(episodeRepository.searchByTerm("Simpson")).thenReturn(episodes);

        assertEquals(episodes, episodeService.getEpisodes("Simpson"));
    }
}
