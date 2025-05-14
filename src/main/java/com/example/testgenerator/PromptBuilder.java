package com.example.testgenerator;

import java.lang.reflect.Method;

public class PromptBuilder {

    public static String buildPrompt(Method method) {
        return "Generate a JUnit 5 test case for the following method:\n\n"
                + method.toString() + "\n\n"
                + "Include necessary mocks using Mockito.";
    }
}
