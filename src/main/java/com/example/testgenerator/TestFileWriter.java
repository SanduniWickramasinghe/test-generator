package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
public class TestFileWriter {

    private static final String PACKAGE_LINE = "package com.example.testgenerator;\n\n";
    private static final List<String> IMPORTS = List.of(
            "import org.junit.jupiter.api.Test;",
            "import static org.junit.jupiter.api.Assertions.*;",
            "import static org.mockito.Mockito.*;"
    );

    public static void writeTestFile(String className, List<String> testMethodContents) throws IOException {
        String directoryPath = "src/test/java/com/example/testgenerator/";
        String testFileName = className + "Test.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + directoryPath);
            }
        }

        File file = new File(directoryPath + testFileName);

        // Delete the file if it already exists
        if (file.exists() && !file.delete()) {
            throw new IOException("Failed to delete existing test file: " + file.getAbsolutePath());
        }

        StringBuilder contentBuilder = new StringBuilder();

        // Append package and import statements
        contentBuilder.append(PACKAGE_LINE);
        IMPORTS.forEach(importLine -> contentBuilder.append(importLine).append("\n"));
        contentBuilder.append("\npublic class ").append(className).append("Test {\n\n");

        Set<String> methodNames = new HashSet<>();

        for (String methodContent : testMethodContents) {
            String methodName = extractMethodName(methodContent);
            if (methodName != null && methodNames.add(methodName)) {
                contentBuilder.append(methodContent).append("\n\n");
            }
        }

        contentBuilder.append("}");
        try (FileWriter writer = new FileWriter(file)) {
            String prompt = PromptBuilder.buildRefactorPrompt(contentBuilder.toString());
            log.info("Start refactoring the code. Prompt : {}", prompt);
            String refactoredContent = OpenAIClient.refactorTestCode(prompt);
            writer.write(refactoredContent);
        }

        log.info("Test written to: {}", file.getAbsolutePath());
    }

    public static void writeTestFileForOpenAPI(String className, String testMethodContents) throws IOException {
        String directoryPath = "src/test/java/com/example/generated/";
        String testFileName = className + "Test.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                throw new IOException("Failed to create directory: " + directoryPath);
            }
        }

        File file = new File(directoryPath + testFileName);

        // Delete the file if it already exists
        if (file.exists() && !file.delete()) {
            throw new IOException("Failed to delete existing test file: " + file.getAbsolutePath());
        }

        StringBuilder contentBuilder = new StringBuilder();

        // Append package and import statements
        contentBuilder.append(PACKAGE_LINE);
        IMPORTS.forEach(importLine -> contentBuilder.append(importLine).append("\n"));
        contentBuilder.append("\npublic class ").append(className).append("Test {\n\n");

        Set<String> methodNames = new HashSet<>();
        contentBuilder.append(testMethodContents).append("\n\n");

        contentBuilder.append("}");

        try (FileWriter writer = new FileWriter(file)) {
            String prompt = PromptBuilder.buildRefactorPrompt(contentBuilder.toString());
            String refactoredContent = OpenAIClient.refactorTestCode(prompt);
            writer.write(refactoredContent);
        }

        log.info("Test written to: {}", file.getAbsolutePath());
    }

    private static String extractMethodName(String methodContent) {
        // Simple regex to extract method name from the method signature
        // Assumes the method signature is in the format: @Test public void methodName() {
        String[] lines = methodContent.split("\\n");
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("public void") || line.startsWith("void")) {
                String[] parts = line.split("\\s+");
                for (int i = 0; i < parts.length; i++) {
                    if (parts[i].equals("void") && i + 1 < parts.length) {
                        String methodName = parts[i + 1];
                        if (methodName.contains("(")) {
                            return methodName.substring(0, methodName.indexOf('('));
                        }
                    }
                }
            }
        }
        return null;
    }
}

