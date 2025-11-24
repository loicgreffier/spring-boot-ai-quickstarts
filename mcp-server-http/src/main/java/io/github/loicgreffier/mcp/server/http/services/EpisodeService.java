package io.github.loicgreffier.mcp.server.http.services;

import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import static io.github.loicgreffier.mcp.server.http.data.McpData.EPISODES;

@Service
public class EpisodeService {

    /**
     * Constructor. The vector store is pre-populated with documents containing information about the Simpsons episodes.
     *
     * @param vectorStore The vector store
     */
    public EpisodeService(VectorStore vectorStore) {
        Resource resource = new ClassPathResource("episodes.csv");
        TikaDocumentReader reader = new TikaDocumentReader(resource);
        vectorStore.add(reader.get());
    }
}
