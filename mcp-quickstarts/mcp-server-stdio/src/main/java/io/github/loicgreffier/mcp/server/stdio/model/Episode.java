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
import java.util.Date;

@Entity
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

    /**
     * Create a new builder.
     *
     * @return The builder
     */
    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getAirdate() {
        return airdate;
    }

    public void setAirdate(Date airdate) {
        this.airdate = airdate;
    }

    public Integer getEpisodeNumber() {
        return episodeNumber;
    }

    public void setEpisodeNumber(Integer episodeNumber) {
        this.episodeNumber = episodeNumber;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSeason() {
        return season;
    }

    public void setSeason(Integer season) {
        this.season = season;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    /** Builder for {@link Episode}. */
    public static class Builder {
        private Long id;
        private Date airdate;
        private Integer episodeNumber;
        private String imagePath;
        private String name;
        private Integer season;
        private String synopsis;

        /**
         * Set the episode ID.
         *
         * @param id The episode ID
         * @return This builder
         */
        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        /**
         * Set the air date.
         *
         * @param airdate The air date
         * @return This builder
         */
        public Builder airdate(Date airdate) {
            this.airdate = airdate;
            return this;
        }

        /**
         * Set the episode number.
         *
         * @param episodeNumber The episode number
         * @return This builder
         */
        public Builder episodeNumber(Integer episodeNumber) {
            this.episodeNumber = episodeNumber;
            return this;
        }

        /**
         * Set the image path.
         *
         * @param imagePath The image path
         * @return This builder
         */
        public Builder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        /**
         * Set the episode name.
         *
         * @param name The episode name
         * @return This builder
         */
        public Builder name(String name) {
            this.name = name;
            return this;
        }

        /**
         * Set the season number.
         *
         * @param season The season number
         * @return This builder
         */
        public Builder season(Integer season) {
            this.season = season;
            return this;
        }

        /**
         * Set the episode synopsis.
         *
         * @param synopsis The episode synopsis
         * @return This builder
         */
        public Builder synopsis(String synopsis) {
            this.synopsis = synopsis;
            return this;
        }

        /**
         * Build the episode.
         *
         * @return The episode
         */
        public Episode build() {
            return new Episode(id, airdate, episodeNumber, imagePath, name, season, synopsis);
        }
    }
}
