package com.example.testgenerator.controller;

import com.example.testgenerator.service.DummySpringService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DummySpringController {

    private final DummySpringService dummySpringService;

    @GetMapping("/greet")
    public String greet(@RequestParam String name) {
        return dummySpringService.greet(name);
    }

    @GetMapping("/number")
    public String getNumber(@RequestParam Integer number) {
        return dummySpringService.getNumber(number);
    }
}
