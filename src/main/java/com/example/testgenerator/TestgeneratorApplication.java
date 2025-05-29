package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@SpringBootApplication
public class TestgeneratorApplication {

	public static void main(String[] args) throws IOException {
		String basePackage = "com.example.testgenerator.app";

		try (ScanResult scanResult = new ClassGraph()
				.acceptPackages(basePackage)
				.enableClassInfo()         // Enables class metadata (required to load classes)
				.enableAnnotationInfo()    // Enables annotation scanning (required to find @RestController, etc.)
				.scan()) {

			List<Class<?>> controllerClasses = scanResult
					.getClassesWithAnnotation(RestController.class.getName())
					.loadClasses();

			List<Class<?>> serviceClasses = scanResult
					.getClassesWithAnnotation(Service.class.getName())
					.loadClasses();

			List<Class<?>> classes = new ArrayList<>();
			classes.addAll(serviceClasses);
			classes.addAll(controllerClasses);

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
