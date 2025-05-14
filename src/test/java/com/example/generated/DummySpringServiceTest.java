package com.example.generated;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@Test
void testExample() {
    // Mock setup
    DummySpringService service = mock(DummySpringService.class);
    // When-Then
    when(service.greet("Adam")).thenReturn("Hello, Adam");
    // Assertions
    assertEquals("mocked", service.greet("Adam"));
}
