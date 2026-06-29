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
package io.github.loicgreffier.chat.rag.advanced.data;

import com.opencsv.CSVReaderHeaderAware;
import com.opencsv.exceptions.CsvValidationException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;

public class RagData {
    private static final String NAME = "name";
    private static final String SYNOPSIS = "synopsis";

    /**
     * Load episodes from the CSV file.
     *
     * @return The list of episodes
     * @throws CsvValidationException If the CSV file is invalid
     * @throws IOException If the CSV file cannot be read
     */
    public static List<Document> loadEpisodes() throws CsvValidationException, IOException {
        List<Document> episodes = new ArrayList<>();

        try (CSVReaderHeaderAware csvReader = new CSVReaderHeaderAware(new InputStreamReader(
                new ClassPathResource("episodes.csv").getInputStream(), StandardCharsets.UTF_8))) {
            Map<String, String> line;
            while ((line = csvReader.readMap()) != null) {
                Map<String, Object> episode = Map.of(
                        "id",
                        line.get("id"),
                        "air_date",
                        line.get("airdate"),
                        "episode_number",
                        line.get("episode_number"),
                        "image_path",
                        line.get("image_path"),
                        NAME,
                        line.get(NAME),
                        "season",
                        line.get("season"),
                        SYNOPSIS,
                        line.get(SYNOPSIS));

                String content = "Name: %s%nSynopsis: %s".formatted(line.get(NAME), line.get(SYNOPSIS));

                episodes.add(Document.builder()
                        .id(UUID.nameUUIDFromBytes(line.get("id").getBytes()).toString())
                        .text(content)
                        .metadata(episode)
                        .build());
            }
        }

        return episodes;
    }

    private RagData() {}
}
