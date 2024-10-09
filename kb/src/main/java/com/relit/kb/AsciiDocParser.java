package com.relit.kb;

import org.asciidoctor.Asciidoctor;
import org.asciidoctor.Options;
import org.asciidoctor.OptionsBuilder;
import org.asciidoctor.ast.Document;
import org.asciidoctor.ast.StructuralNode;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.asciidoctor.Options.builder;

@Component
public class AsciiDocParser {

    private final Asciidoctor asciidoctor;

    public AsciiDocParser() {
        this.asciidoctor = Asciidoctor.Factory.create();
    }

    public List<String> parseContent(String content) {
        Document document = asciidoctor.load(content, builder().build());
        List<String> chunks = new ArrayList<>();

        Map<Object, Object> selector = new HashMap<>();
        selector.put("context", ":section");

        List<StructuralNode> sections = document.findBy(selector);

        for (StructuralNode section : sections) {
            String title = section.getTitle();
            String sectionContent = section.getContent().toString();
            chunks.add(title + "\n\n" + sectionContent);
        }

        return chunks;
    }
}
