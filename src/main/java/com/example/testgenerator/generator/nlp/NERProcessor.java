package com.example.testgenerator.generator.nlp;

import edu.stanford.nlp.pipeline.*;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This Spring service performs Named Entity Recognition (NER)
 * using Stanford CoreNLP library.
 */
@Service
public class NERProcessor {

    private final StanfordCoreNLP pipeline;

    public NERProcessor() {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,ssplit,pos,lemma,ner");
        this.pipeline = new StanfordCoreNLP(props);
    }

    /**
     * This method extracts named entities from the given text.
     *
     * @param text The input text to analyze (e.g., a class or method comment)
     * @return A map where the key is the entity type (e.g., PERSON, ORGANIZATION)
     *         and the value is a set of all recognized entities of that type
     */
    public Map<String, Set<String>> extractEntities(String text) {
        // Map to hold entity types and corresponding detected values
        Map<String, Set<String>> entities = new HashMap<>();

        // Wrap the input text in a CoreDocument for annotation
        CoreDocument document = new CoreDocument(text);

        // Run the pipeline annotators on the document
        pipeline.annotate(document);

        // Loop through all entity mentions found in the text
        for (CoreEntityMention em : document.entityMentions()) {
            // Group the entity by its type and add the actual text to the set
            entities
                    .computeIfAbsent(em.entityType(), k -> new HashSet<>()) // create set if it doesn't exist
                    .add(em.text()); // add the recognized entity text
        }

        return entities;
    }
}

