package com.example.testgenerator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.parameters.Parameter;

import java.lang.reflect.Method;

public class PromptBuilder {

    public static String buildPrompt(Method method) {
        return "Generate a JUnit 5 test case for the following method:\n\n"
                + method.toString() + "\n\n"
                + "Include necessary mocks using Mockito.";
    }

    public static String buildRefactorPrompt(String content){
        return "Refactor this class with proper import structure and brackets. Append all methods in proper places:\n\n"
                + content;
    }

    public static String buildOpenAPIPrompt(OpenAPI openAPI) {
        StringBuilder prompt = new StringBuilder("Generate JUnit 5 test cases for a Spring Boot controller with the following endpoints:\n");

        if (openAPI.getPaths() == null) return "No paths found.";

        openAPI.getPaths().forEach((path, pathItem) -> {
            pathItem.readOperationsMap().forEach((httpMethod, operation) -> {
                prompt.append("- [").append(httpMethod).append("] ").append(path);

                if (operation.getSummary() != null) {
                    prompt.append(" - ").append(operation.getSummary());
                }

                if (operation.getParameters() != null && !operation.getParameters().isEmpty()) {
                    prompt.append(" (Parameters: ");
                    for (Parameter param : operation.getParameters()) {
                        prompt.append(param.getName()).append(": ").append(param.getSchema().getType()).append(", ");
                    }
                    prompt.setLength(prompt.length() - 2); // trim trailing comma
                    prompt.append(")");
                }

                prompt.append("\n");
            });
        });

        prompt.append("\nInclude mocked service layer using Mockito.");
        return prompt.toString();
    }
}
