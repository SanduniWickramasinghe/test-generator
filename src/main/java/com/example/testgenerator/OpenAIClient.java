package com.example.testgenerator;

public class OpenAIClient {

    public static String generateTestCode(String prompt) {
        // Simulate an API call
        return """
               @Test
               void testExample() {
                   // Mock setup
                   DummySpringService service = mock(DummySpringService.class);
                   // When-Then
                   when(service.greet("Adam")).thenReturn("Hello, Adam");
                   // Assertions
                   assertEquals("mocked", service.greet("Adam"));
               }
               """;
    }
}
