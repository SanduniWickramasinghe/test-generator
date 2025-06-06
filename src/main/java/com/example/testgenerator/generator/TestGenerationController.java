package com.example.testgenerator.generator;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/api/test-gen")
public class TestGenerationController {

    private final TestGenerationService testGenerationService;

    @PostMapping("/generate")
    public String generateTests(@RequestParam("source") String source) {
        try {
            testGenerationService.generateTestsFromOpenAPI(source);
            return "Test generation completed.";
        } catch (Exception e) {
            return "Test generation failed: " + e.getMessage();
        }
    }
}