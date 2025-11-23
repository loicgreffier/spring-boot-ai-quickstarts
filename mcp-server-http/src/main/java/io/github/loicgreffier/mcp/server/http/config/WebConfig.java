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
package io.github.loicgreffier.mcp.server.http.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/** Web configuration. */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Define the security filter chain.
     *
     * @param http The http security
     * @return The security filter chain
     * @throws Exception If something goes wrong
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer ->
                        sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequestsConfigurer -> authorizeRequestsConfigurer
                        // Back-End
                        .requestMatchers("/chat/**")
                        .permitAll()
                        .anyRequest()
                        .denyAll());

        return http.build();
    }

    /** Rest controller advice used to manage exceptions. */
    @Slf4j
    @RestControllerAdvice
    public static class GlobalDefaultExceptionHandler {
        @ExceptionHandler(Exception.class)
        public ResponseEntity<Void> handleException(Exception exception) {
            log.error("An exception has occurred in the API controllers part", exception);

            return ResponseEntity.internalServerError().build();
        }
    }
}
