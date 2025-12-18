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

import io.github.loicgreffier.mcp.server.stdio.repository.EpisodeRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

@Service
public class EpisodeService {
    private final EpisodeRepository episodeRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param episodeRepository The episode repository
     */
    public EpisodeService(EpisodeRepository episodeRepository) {
        this.episodeRepository = episodeRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get episodes.
     *
     * @param searchTerm The episode name or synopsis to search for
     * @return The episode details
     */
    @Tool(name = "get_episodes", description = "Get episodes by name or synopsis search term.")
    public String getEpisodes(
            @ToolParam(
                            description =
                                    "The search term to look for in episode names or synopses. It can be a partial name or a partial section of the synopsis.")
                    String searchTerm)
            throws JacksonException {
        return objectMapper.writeValueAsString(episodeRepository.searchByTerm(searchTerm));
    }
}
