package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;

@Slf4j
@SpringBootApplication
public class TestgeneratorApplication {

	public static void main(String[] args) throws IOException {
		String packageName = "com.example.testgenerator.service";
		try (ScanResult scanResult = new ClassGraph()
				.acceptPackages(packageName)
				.scan()) {
			List<Class<?>> classes = scanResult.getAllClasses().loadClasses();
			for (Class<?> clazz : classes) {
				List<Method> publicMethods = MethodScanner.getPublicMethods(clazz);
				log.info("PublicMethods in {}: {}", clazz.getSimpleName(), publicMethods);

				List<String> generatedTestList = new ArrayList<>();

				for (Method method : publicMethods) {
					String prompt = PromptBuilder.buildPrompt(method);
					log.info("Prompt: {}", prompt);
					String generatedTest = OpenAIClient.generateTestCode(prompt);
					log.info("GeneratedTest: {}", generatedTest);
					generatedTestList.add(generatedTest);
				}
				TestFileWriter.writeTestFile(clazz.getSimpleName(), generatedTestList);
			}
		}
	}
}
