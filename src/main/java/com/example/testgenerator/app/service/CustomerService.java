package com.example.testgenerator.app.service;

import com.example.testgenerator.app.dto.CustomerDto;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    public String greet(String name) {
        return "Hello, " + name;
    }

    public String getNumber(Integer number) {
        return String.valueOf(number);
    }

    public CustomerDto getCustomer(){
        return CustomerDto.builder()
                .id(1)
                .name("UOP User")
                .build();
    }
}
