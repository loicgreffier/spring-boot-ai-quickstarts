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
package io.github.loicgreffier.mcp.server.stdio.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.sql.Date;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Episode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date airdate;

    private Integer episodeNumber;
    private String imagePath;
    private String name;
    private Integer season;

    @Column(length = 2000)
    private String synopsis;

    /** Default constructor. */
    public Episode() {}

    /**
     * Constructor.
     *
     * @param id The episode ID
     * @param airdate The air date
     * @param episodeNumber The episode number
     * @param imagePath The image path
     * @param name The episode name
     * @param season The season number
     * @param synopsis The episode synopsis
     */
    public Episode(
            Long id,
            Date airdate,
            Integer episodeNumber,
            String imagePath,
            String name,
            Integer season,
            String synopsis) {
        this.id = id;
        this.airdate = airdate;
        this.episodeNumber = episodeNumber;
        this.imagePath = imagePath;
        this.name = name;
        this.season = season;
        this.synopsis = synopsis;
    }
}
