package com.relit.kb;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;
    private final AsciiDocParser asciiDocParser;

    public RagService(ChatClient.Builder chatClientBuilder, VectorStore vectorStore, AsciiDocParser asciiDocParser) {
        this.chatClient = chatClientBuilder.build();
        this.vectorStore = vectorStore;
        this.asciiDocParser = asciiDocParser;
    }

    public void processAndStoreDocument(String content) {
        List<String> chunks = asciiDocParser.parseContent(content);
        for (String chunk : chunks) {
            Document document = new Document(chunk);
            vectorStore.add(List.of(document));
        }
    }

    public String query(String question) {
        List<Document> relevantDocs = vectorStore.similaritySearch(SearchRequest.query(question).withTopK(3));
        String context = relevantDocs.stream()
                .map(Document::getContent)
                .reduce((a, b) -> a + "\n\n" + b)
                .orElse("");

        String prompt = "Based on the following documents, answer the question:\n\n" +
                "Context: " + context + "\n\n" +
                "Question: " + question;

        ChatResponse response = chatClient.prompt(new Prompt(prompt)).call().chatResponse();
        return response.getResult().getOutput().getContent();
    }
}
