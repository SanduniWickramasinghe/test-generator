package com.example.testgenerator.generator;

import com.example.testgenerator.generator.external.OpenAIClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class TestGenerationService {

    private final TestFileWriter testFileWriter;
    private final PromptBuilder promptBuilder;

    public void generateTestsFromOpenAPI(String sourceRoot) throws IOException {
        try (Stream<Path> paths = Files.walk(Paths.get(sourceRoot))) {
            List<Path> javaFiles = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".java"))
                    .toList();

            for (Path javaFile : javaFiles) {
                try {
                    String content = Files.readString(javaFile, StandardCharsets.UTF_8);

                    if (containsControllerOrServiceAnnotation(content)) {
                        String prompt = promptBuilder.buildPromptForClass(content, sourceRoot);
                        System.out.println("Prompt: " + prompt);

                        String generatedTest = OpenAIClient.generateTestCode(prompt);
                        System.out.println("Generated Test: " + generatedTest);

                        testFileWriter.writeTestFile(generatedTest, getTestPath(javaFile));
                    }

                } catch (IOException e) {
                    System.err.println("Failed to process file: " + javaFile + " — " + e.getMessage());
                }
            }
        }
    }

    private String getTestPath(Path javaFile) {
        String sourcePath = javaFile.toAbsolutePath().toString();
        String testPath = sourcePath.replace("src\\main\\java", "src\\test\\java");

        if (testPath.endsWith(".java")) {
            testPath = testPath.replace(".java", "Test.java");
        }

        return testPath;
    }

    private boolean containsControllerOrServiceAnnotation(String content) {
        return content.contains("@RestController") || content.contains("@Service");
    }

    public static Map<Class<?>, String> loadClassSourceMap(String baseDir, List<Class<?>> allClasses) {
        Map<Class<?>, String> sourceMap = new HashMap<>();
        for (Class<?> clazz : allClasses) {
            String path = baseDir + "/" + clazz.getName().replace(".", "/") + ".java";
            try {
                String source = Files.readString(Paths.get(path));
                sourceMap.put(clazz, source);
            } catch (IOException e) {
                log.warn("Could not read source file for class: {}", clazz.getName());
            }
        }
        return sourceMap;
    }
}
