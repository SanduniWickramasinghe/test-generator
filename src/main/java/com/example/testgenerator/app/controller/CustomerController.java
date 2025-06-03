package com.example.testgenerator.app.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.example.testgenerator.app.dto.CustomerDto;
import com.example.testgenerator.app.service.CustomerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
@Tag(name = "Dummy API", description = "Endpoints for greeting and retrieving dummy data")
public class CustomerController {

    private final CustomerService customerService;

    @Operation(summary = "Greet user", description = "Returns a greeting message for the given name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned greeting"),
            @ApiResponse(responseCode = "400", description = "Invalid name parameter")
    })
    @GetMapping("/greet")
    public String greet(
            @Parameter(description = "Name of the user to greet")
            @RequestParam String name) {
        return customerService.greet(name);
    }

    @Operation(summary = "Echo number", description = "Returns the number as a string")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned number"),
            @ApiResponse(responseCode = "400", description = "Invalid number parameter")
    })
    @GetMapping("/number")
    public String getNumber(
            @Parameter(description = "Number to echo")
            @RequestParam Integer number) {
        return customerService.getNumber(number);
    }

    @Operation(summary = "Get Customer", description = "Returns a dummy Customer object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully returned customer")
    })
    @GetMapping("/customer")
    public CustomerDto getCustomer() {
        return customerService.getCustomer();
    }
}
