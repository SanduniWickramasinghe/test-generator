package com.example.testgenerator.app.service;

import org.springframework.stereotype.Service;

@Service
public class DummySpringService {

    public String greet(String name) {
        return "Hello, " + name;
    }

    public String getNumber(Integer number) {
        return String.valueOf(number);
    }

    public Object getObj(){
        return null;
    }
}
