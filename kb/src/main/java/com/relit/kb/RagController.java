package com.relit.kb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final MarkDownService markDownService;

    public RagController(RagService ragService, MarkDownService markDownService) {
        this.ragService = ragService;
        this.markDownService = markDownService;
    }

    @PostMapping("/process")
    public void processDocument(@RequestBody String content) {
        ragService.processAndStoreDocument(content);
    }

    @GetMapping("/query")
    public String query(@RequestParam String question) {
        return markDownService.markdownToHtml(ragService.query(question));
    }
}