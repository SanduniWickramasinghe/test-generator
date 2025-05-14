package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

@Slf4j
public class TestFileWriter {

    private static final String PACKAGE_LINE = "package com.example.generated;\n\n";
    private static final String IMPORTS = """
        import org.junit.jupiter.api.Test;
        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;
        """;

    public static void writeTestFile(String className, List<String> testMethodContent) throws IOException {
        String directoryPath = "src/test/java/com/example/generated/";
        String testFileName = className + "Test.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directoryPath + testFileName);
        StringBuilder contentBuilder = new StringBuilder();

        contentBuilder.append(PACKAGE_LINE)
                .append(IMPORTS)
                .append("\npublic class ")
                .append(className)
                .append("Test {\n\n");
        for(String content : testMethodContent){
            contentBuilder.append(content)
                    .append("\n");
        }
        contentBuilder.append("}");

        try (FileWriter writer = new FileWriter(file, false)) {
            writer.write(contentBuilder.toString());
        }

        log.info("Test written to: {}", file.getAbsolutePath());
    }
}
