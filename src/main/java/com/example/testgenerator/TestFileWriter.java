package com.example.testgenerator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFileWriter {

    private static final String PACKAGE_LINE = "package com.example.generated;\n\n";
    private static final String IMPORTS = """
        import org.junit.jupiter.api.Test;
        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;
        """;

    public static void writeTestFile(String className, String testMethodContent) throws IOException {
        String directoryPath = "src/test/java/com/example/generated/";
        String testFileName = className + "Test.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directoryPath + testFileName);
        StringBuilder contentBuilder = new StringBuilder();

        if (!file.exists()) {
            // New file: write full structure
            contentBuilder.append(PACKAGE_LINE)
                    .append(IMPORTS)
                    .append("\npublic class ")
                    .append(className)
                    .append("Test {\n\n");
        } else {
            // Existing file: load existing content
            contentBuilder.append(Files.readString(file.toPath()));
            // Remove the final closing brace to append new test methods
            contentBuilder.setLength(contentBuilder.length() - 1);
        }

        // Append generated test method and closing brace
        contentBuilder.append("\n")
                .append(testMethodContent)
                .append("\n}");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(contentBuilder.toString());
        }

        System.out.println("Test written to: " + file.getAbsolutePath());
    }
}
