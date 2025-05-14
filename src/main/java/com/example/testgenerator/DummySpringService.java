package com.example.testgenerator;

import org.springframework.stereotype.Service;

@Service
public class DummySpringService {

    public String greet(String name) {
        return "Hello, " + name;
    }

    public String getNumber(Integer number) {
        return String.valueOf(number);
    }
}
