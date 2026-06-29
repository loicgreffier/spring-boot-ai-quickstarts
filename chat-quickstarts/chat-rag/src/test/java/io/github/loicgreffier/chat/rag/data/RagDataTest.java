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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.ai.document.Document;

class RagDataTest {

    @Test
    void shouldLoadEpisodes() throws Exception {
        List<Document> results = RagData.loadEpisodes();

        assertEquals(768, results.size());
        assertEquals("c4ca4238-a0b9-3382-8dcc-509a6f75849b", results.getFirst().getId());
        assertEquals(
                ("Name: Simpsons Roasting on an Open Fire%nSynopsis: When Mr. Burns announces that none of the "
                                + "workers will be getting Christmas bonuses and Marge reveals that she spent the extra "
                                + "Christmas gift money on getting Bart's \"\"Mother\"\" tattoo removed, Homer keeps his "
                                + "lack of funds for the holidays a secret and gets a job as a mall Santa.")
                        .formatted(),
                results.getFirst().getText());
        assertEquals("3a835d32-1575-3c43-9ef4-fe9965a3f2a0", results.getLast().getId());
        assertEquals(
                ("Name: Bart's Brain%nSynopsis: Bart buys a brain in a jar from Herman's Military Antique Store, "
                                + "and his attachment to it causes concern for everyone around him.")
                        .formatted(),
                results.getLast().getText());
    }
}
