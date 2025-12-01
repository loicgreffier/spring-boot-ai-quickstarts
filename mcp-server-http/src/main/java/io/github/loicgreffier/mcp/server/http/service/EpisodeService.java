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
package io.github.loicgreffier.mcp.server.http.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.loicgreffier.mcp.server.http.data.McpData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EpisodeService {
    private static final Double SIMILARITY_SEARCH = 0.5;
    private static final Integer TOP_K = 5;
    private final VectorStore vectorStore;
    private final ObjectMapper objectMapper;

    /**
     * Constructor. The vector store is pre-populated with documents containing information about the Simpsons episodes.
     *
     * @param vectorStore The vector store
     */
    public EpisodeService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.objectMapper = new ObjectMapper();

        long startTime = System.currentTimeMillis();
        vectorStore.add(McpData.loadEpisodes());
        double duration = (System.currentTimeMillis() - startTime) / 1000.0;
        log.info(
                "Loaded {} episodes into the vector store in {}s",
                McpData.loadEpisodes().size(),
                duration);
    }

    /**
     * Get episodes.
     *
     * @param searchTerm The description to search for
     * @return The episode details
     */
    @Tool(
            name = "get_episodes",
            description = "Get episodes by description by performing a similarity search on episode synopses.")
    public String getEpisodes(
            @ToolParam(description = "The episode description used to perform the similarity search.")
                    String searchTerm)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(vectorStore.similaritySearch(SearchRequest.builder()
                .similarityThreshold(SIMILARITY_SEARCH)
                .topK(TOP_K)
                .query(searchTerm)
                .build()));
    }
}
