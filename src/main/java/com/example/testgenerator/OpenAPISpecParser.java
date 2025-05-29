package com.example.testgenerator;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.parser.OpenAPIV3Parser;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class OpenAPISpecParser {

    public static void main(String[] args) throws IOException {
        String specFilePath = "src/main/resources/openapi.yml";
        OpenAPI openAPI = new OpenAPIV3Parser().read(specFilePath);

        String prompt = PromptBuilder.buildOpenAPIPrompt(openAPI);
        log.info("Prompt: {}", prompt);
        String generatedTest = OpenAIClient.generateTestCode(prompt);

        TestFileWriter.writeTestFileForOpenAPI("sample", generatedTest);

    }


}
