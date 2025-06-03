package com.example.testgenerator.app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Customer entity")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    @Schema(description = "Customer ID", example = "101")
    private int id;

    @Schema(description = "Customer name", example = "Alice")
    private String name;
}
