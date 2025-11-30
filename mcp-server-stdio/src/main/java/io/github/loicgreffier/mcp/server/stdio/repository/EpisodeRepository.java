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
package io.github.loicgreffier.mcp.server.stdio.repository;

import io.github.loicgreffier.mcp.server.stdio.model.Episode;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EpisodeRepository extends CrudRepository<Episode, Long> {
    /** Find episodes by name or synopsis containing the given term, case-insensitive. */
    @Query("SELECT e FROM Episode e WHERE LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) "
            + "OR LOWER(e.synopsis) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Episode> searchByTerm(@Param("searchTerm") String searchTerm);
}
