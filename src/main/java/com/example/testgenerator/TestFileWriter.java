package com.example.testgenerator;

import java.io.*;
import java.nio.file.*;

public class TestFileWriter {

    private static final String DIRECTORY_PATH = "src/test/java/com/example/generated/";

    public static void writeTestFile(String className, String methodTestContent) throws IOException {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(DIRECTORY_PATH + className + "Test.java");
        boolean fileExists = file.exists();

        StringBuilder fileContent = new StringBuilder();

        if (!fileExists) {
            fileContent.append("""
                    package com.example.generated;

                    import org.junit.jupiter.api.Test;
                    import static org.mockito.Mockito.*;
                    import static org.junit.jupiter.api.Assertions.*;

                    public class """ + className + "Test {\n\n");
        } else {
            fileContent.append(Files.readString(file.toPath()));
            // Remove closing brace if already there
            int lastBrace = fileContent.lastIndexOf("}");
            if (lastBrace != -1) {
                fileContent.delete(lastBrace, fileContent.length());
            }
        }

        fileContent.append(methodTestContent).append("\n\n").append("}");

        try (FileWriter writer = new FileWriter(file)) {
            writer.write(fileContent.toString());
        }

        System.out.println("Test updated: " + file.getAbsolutePath());
    }
}
