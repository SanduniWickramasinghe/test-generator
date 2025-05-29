package com.example.testgenerator.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class Customer {
    private Integer id;
    private String name;
}
