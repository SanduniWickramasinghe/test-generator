package com.example.testgenerator.generator;

import com.example.testgenerator.generator.nlp.NERProcessor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class PromptBuilder {
    private static final String SRC_ROOT = "src/main/java/";

    private final NERProcessor nerProcessor;

    public PromptBuilder(NERProcessor nerProcessor) {
        this.nerProcessor = nerProcessor;
    }

    public String buildPromptForClass(String mainClassContent) throws IOException {
        StringBuilder prompt = new StringBuilder();

        // Append main class header
        prompt.append("Generate a JUnit 5 test case for the following Spring service class.\n")
                .append("Include necessary mocks using Mockito.\n\n")
                .append("Main Class:\n")
                .append("===================\n")
                .append(mainClassContent)
                .append("\n\n");

        // Perform NER
        Map<String, Set<String>> entities = nerProcessor.extractEntities(mainClassContent);

        // Append extracted entities to the prompt
        prompt.append("Extracted Entities:\n")
                .append("===================\n");
        for (Map.Entry<String, Set<String>> entry : entities.entrySet()) {
            prompt.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
        }

        // Extract import statements of custom classes
        List<String> customClassPaths = extractCustomImports(mainClassContent);

        // Load each custom class source and append to prompt
        for (String classPath : customClassPaths) {
            String classSource = loadClassSource(classPath);
            if (classSource != null) {
                prompt.append("Related Model Class:\n")
                        .append("===================\n")
                        .append(classSource)
                        .append("\n\n");
            }
        }

        return prompt.toString();
    }

    /**
     * Extract fully qualified class names from import statements
     * that are likely custom (exclude java.*, javax.*, org.springframework.*, etc.)
     *
     * @param source Java source code text
     * @return list of fully qualified class names to load
     */
    private static List<String> extractCustomImports(String source) {
        Pattern importPattern = Pattern.compile("^import\\s+([a-zA-Z0-9_.]+);", Pattern.MULTILINE);
        Matcher matcher = importPattern.matcher(source);

        List<String> imports = new ArrayList<>();
        while (matcher.find()) {
            String imp = matcher.group(1);
            // Filter out standard library and common third-party packages
            if (!imp.startsWith("java.") && !imp.startsWith("javax.") &&
                    !imp.startsWith("org.springframework") && !imp.startsWith("org.junit")) {
                imports.add(imp);
            }
        }
        return imports;
    }

    /**
     * Load the source file of a class given its fully qualified name.
     * E.g. "com.example.testgenerator.app.model.Customer"
     * -> "src/main/java/com/example/testgenerator/app/model/Customer.java"
     *
     * @param fullyQualifiedClassName class full name
     * @return source code as string, or null if file not found
     * @throws IOException
     */
    private static String loadClassSource(String fullyQualifiedClassName) throws IOException {
        String path = SRC_ROOT + fullyQualifiedClassName.replace('.', '/') + ".java";
        Path classFilePath = Paths.get(path);
        if (Files.exists(classFilePath)) {
            return Files.readString(classFilePath, StandardCharsets.UTF_8);
        } else {
            System.err.println("Warning: Could not find source file for " + fullyQualifiedClassName);
            return null;
        }
    }


    public static String buildRefactorPrompt(String content, Class<?> clazz) {

        return "You are a Java expert. Refactor the following Java class source code:\n"
                + "- Ensure proper import statements, no duplicates, and no unused imports.\n"
                + "- Fix all formatting issues, including indentation and bracket placement.\n"
                + "- Merge all methods correctly inside the class body without extra characters.\n"
                + "- Do NOT add any extra characters or symbols before or after the class or methods.\n"
                + "- Provide the fully formatted Java class code only, without explanations or comments " +
                "or any aditional characters which give compile errors.\n\n"
                + content +
                "\n\nThe following is the referenced class with their package names. Include these properly in imports:\n" +
                "- " + clazz + "\n";
    }

}
