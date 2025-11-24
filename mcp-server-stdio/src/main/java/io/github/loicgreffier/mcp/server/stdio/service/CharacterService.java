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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.loicgreffier.mcp.server.stdio.repository.CharacterRepository;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    /**
     * Constructor.
     *
     * @param characterRepository The character repository
     */
    public CharacterService(CharacterRepository characterRepository) {
        this.characterRepository = characterRepository;
        this.objectMapper = new ObjectMapper();
    }

    /**
     * Get characters by name.
     *
     * @param name The name
     * @return The character details
     */
    @Tool(name = "get_characters_by_name", description = "Get characters by name")
    public String getCharactersByName(
            @ToolParam(description = "The character name to search for. Can be a partial name.") String name)
            throws JsonProcessingException {
        return objectMapper.writeValueAsString(characterRepository.findByNameContainingIgnoreCase(name));
    }
}
