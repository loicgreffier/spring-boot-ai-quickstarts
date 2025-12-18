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
package io.github.loicgreffier.chat.rag.data;

import tools.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.core.io.ClassPathResource;

@Slf4j
public class RagData {

    /**
     * Load episodes from the CSV file.
     *
     * @return The list of episodes
     */
    public static List<Document> loadEpisodes() {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Document> episodes = new ArrayList<>();

        try (CSVReader csvReader = new CSVReader(new FileReader(new ClassPathResource("episodes.csv").getFile()))) {
            String[] _ = csvReader.readNext();

            String[] line;
            while ((line = csvReader.readNext()) != null) {
                Map<String, Object> episode = Map.of(
                        "id", line[0],
                        "air_date", line[1],
                        "episode_number", line[2],
                        "image_path", line[3],
                        "title", line[4],
                        "season", line[5],
                        "synopsis", line[6]);

                episodes.add(Document.builder()
                        .id(UUID.nameUUIDFromBytes(line[0].getBytes()).toString())
                        .text(objectMapper.writeValueAsString(episode))
                        .metadata(episode)
                        .build());
            }
        } catch (CsvValidationException | IOException e) {
            log.error("Error reading episodes.csv", e);
        }

        return episodes;
    }

    private RagData() {}
}
