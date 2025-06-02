package com.example.testgenerator.generator;

import com.example.testgenerator.generator.external.OpenAIClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Slf4j
@Service
public class TestFileWriter {

    private static final String PACKAGE_LINE = "package com.example.testgenerator;\n\n";

    public void writeTestFile(Class<?> clazz, String testMethodContents) throws IOException {
        String directoryPath = "src/test/java/com/example/testgenerator/";
        String testFileName = clazz.getSimpleName() + "Test.java";
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
        contentBuilder.append(testMethodContents);

        contentBuilder.append("}");
        try (FileWriter writer = new FileWriter(file)) {
            String prompt = PromptBuilder.buildRefactorPrompt(contentBuilder.toString(), clazz);
            log.info("Start refactoring the code. Prompt : {}", prompt);
            String refactoredContent = OpenAIClient.refactorTestCode(prompt);
            refactoredContent = cleanLLMResponse(refactoredContent);
            log.info("Refactored and cleaned code : {}", refactoredContent);
            writer.write(refactoredContent);
        }
        log.info("Test written to: {}", file.getAbsolutePath());
    }

    public static String cleanLLMResponse(String rawCode) {
        if (rawCode == null || rawCode.isBlank()) return "";
        String code = rawCode.trim();
        // Remove Markdown code block fences like ```java ... ```
        if (code.startsWith("```")) {
            int first = code.indexOf('\n');
            int last = code.lastIndexOf("```");
            if (first != -1 && last != -1 && first < last) {
                code = code.substring(first + 1, last).trim();
            }
        }
        // Remove leading/trailing backticks or stray characters
        code = code.replaceAll("^`+", "").replaceAll("`+$", "").trim();
        // Normalize bracket usage if needed
        code = code.replaceAll("\\s+\\{", " {");
        return code;
    }

}

