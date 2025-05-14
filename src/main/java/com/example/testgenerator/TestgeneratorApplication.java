package com.example.testgenerator;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

@SpringBootApplication
public class TestgeneratorApplication {

	public static void main(String[] args) throws IOException {
		Class<?> clazz = DummySpringService.class;

		List<Method> publicMethods = MethodScanner.getPublicMethods(clazz);
		System.out.println("PublicMethods : " + publicMethods);

		for (Method method : publicMethods) {
			String prompt = PromptBuilder.buildPrompt(method);
			System.out.println("Prompt : " + prompt);
			String generatedTest = OpenAIClient.generateTestCode(prompt);
			System.out.println("GeneratedTest : " + generatedTest);
			TestFileWriter.writeTestFile(clazz.getSimpleName(), generatedTest);
		}
	}

}
