package com.example.testgenerator;

import java.lang.reflect.Method;

public class PromptBuilder {

    public static String buildPrompt(Method method) {
        return "Generate a JUnit 5 test case for the following method:\n\n"
                + method.toString() + "\n\n"
                + "Include necessary mocks using Mockito.";
    }

    public static String buildPromptForClass(String content) {
        return "Generate a JUnit 5 test case for the following class:\n\n"
                + content + "\n\n"
                + "Include necessary mocks using Mockito.";
    }

    public static String buildRefactorPrompt(String content){
        return "Refactor this class with proper import structure and brackets. Append all methods in proper places. Provide correct imports:\n\n"
                + content;
    }

}
