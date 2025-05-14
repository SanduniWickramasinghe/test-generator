package com.example.testgenerator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootApplication
public class TestgeneratorApplication {

	public static void main(String[] args) throws IOException {
		Class<?> clazz = DummySpringService.class;

		List<Method> publicMethods = MethodScanner.getPublicMethods(clazz);
        log.info("PublicMethods : {}", publicMethods);

		List<String> generatedTestList = new ArrayList<>();

		for (Method method : publicMethods) {
			String prompt = PromptBuilder.buildPrompt(method);
            log.info("Prompt : {}", prompt);
			String generatedTest = OpenAIClient.generateTestCode(prompt);
            log.info("GeneratedTest : {}", generatedTest);
			generatedTestList.add(generatedTest);
		}
		TestFileWriter.writeTestFile(clazz.getSimpleName(), generatedTestList);

	}

}
