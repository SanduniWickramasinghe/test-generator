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
                        String prompt = promptBuilder.buildPromptForClass(content);
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

//    public void generateTestsFromOpenAPI1() throws IOException {
//        String externalSourceRoot ="D://MscProject//sample-applications//category-1//demo//src//main//java//com//example//demo//controller//";
//
//
//        //String basePackage = "com.example.testgenerator.app";
//
////        try (ScanResult scanResult = new ClassGraph()
////                .overrideClasspath(externalSourceRoot)
////                .acceptPackages("com.example.demo.controller")
////                .enableClassInfo()         // Enables class metadata (required to load classes)
////                .enableAnnotationInfo()    // Enables annotation scanning (required to find @RestController, etc.)
////                .scan()) {
//        try (ScanResult scanResult = new ClassGraph()
//                .enableClassInfo()
//                .enableAnnotationInfo()
//                .overrideClasspath(externalSourceRoot)
//                //.acceptPackages("com.example.demo") // your app's base package
//                .scan()) {
//
//            //Identify Controllers using classgraph
//            List<Class<?>> controllerClasses = scanResult
//                    .getClassesWithAnnotation(RestController.class.getName())
//                    .loadClasses();
//
//            //Identify Services using classgraph
//            List<Class<?>> serviceClasses = scanResult
//                    .getClassesWithAnnotation(Service.class.getName())
//                    .loadClasses();
//
//            List<Class<?>> classes = new ArrayList<>();
//            classes.addAll(serviceClasses);
//            classes.addAll(controllerClasses);
//
//            for (Class<?> clazz : classes) {
//                String className = clazz.getSimpleName();
//                String packagePath = clazz.getPackage().getName().replace('.', '/');
//                //String filePath = "src/main/java/" + packagePath + "/" + className + ".java";
//                String filePath = externalSourceRoot + packagePath + "/" + className + ".java";
//                try {
//                    String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
//                    String prompt = promptBuilder.buildPromptForClass(content);
//                    log.info("Prompt: {}", prompt);
//                    String generatedTest = OpenAIClient.generateTestCode(prompt);
//                    log.info("GeneratedTest: {}", generatedTest);
//                    testFileWriter.writeTestFile(clazz, generatedTest);
//                } catch (IOException e) {
//                    log.error("Error reading or writing file for class: " + clazz.getSimpleName() + " - " + e.getMessage());
//                    throw e;
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error during test generation: " + e.getMessage());
//            throw new RuntimeException("Error during test generation", e);
//        }
//    }

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
