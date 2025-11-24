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
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
public class Character {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer age;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "UTC")
    private Date birthDate;

    private String gender;
    private String name;
    private String occupation;
    private String portraitUrl;
    private String status;

    @Builder.Default
    @OneToMany(mappedBy = "character", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<Phrase> phrases = new ArrayList<>();

    /** Default constructor. */
    public Character() {
        this.phrases = new ArrayList<>();
    }

    /**
     * Constructor.
     *
     * @param id The character ID
     * @param age The age
     * @param birthDate The birthdate
     * @param gender The gender
     * @param name The name
     * @param occupation The occupation
     * @param portraitUrl The portrait URL
     * @param status The status
     */
    public Character(
            Long id,
            Integer age,
            Date birthDate,
            String gender,
            String name,
            String occupation,
            String portraitUrl,
            String status,
            List<Phrase> phrases) {
        this.id = id;
        this.age = age;
        this.birthDate = birthDate;
        this.gender = gender;
        this.name = name;
        this.occupation = occupation;
        this.portraitUrl = portraitUrl;
        this.status = status;
        this.phrases = phrases;
    }
}
