package io.github.loicgreffier.mcp.server.http.data;

import org.springframework.ai.document.Document;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class McpData {
    public static final List<Document> EPISODES = List.of(
            new Document(
                    UUID.nameUUIDFromBytes("000".getBytes()).toString(),
                    "The Simpsons is an animated sitcom created by Matt Groening that first aired in 1989.",
                    Map.of())
    );

    private McpData() { }
}

