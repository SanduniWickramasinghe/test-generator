package com.example.testgenerator.generator.external;

import java.io.IOException;

import com.example.testgenerator.generator.OpenAIResponseParser;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.json.JSONObject;

@Slf4j
public class OpenAIClient {

    private static final String API_KEY = "sample-key"; //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

    private static final String API_URL = "https://api.openai.com/v1/chat/completions";

    public static String generateTestCode(String prompt) throws IOException {
        OkHttpClient client = new OkHttpClient();

        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", prompt);

        JSONObject payload = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", new org.json.JSONArray().put(message))
                .put("temperature", 0.3);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                payload.toString()
                );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            String responseBody = response.body().string();
            return OpenAIResponseParser.extractContent(responseBody);
        }
    }

    public static String refactorTestCode(String prompt) throws IOException {

        OkHttpClient client = new OkHttpClient();

        JSONObject message = new JSONObject()
                .put("role", "user")
                .put("content", prompt);

        JSONObject payload = new JSONObject()
                .put("model", "gpt-3.5-turbo")
                .put("messages", new org.json.JSONArray().put(message))
                .put("temperature", 0.3);

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                payload.toString()
        );

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", "Bearer " + API_KEY)
                .addHeader("Content-Type", "application/json")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected response: " + response);
            }
            assert response.body() != null;
            String responseBody = response.body().string();
            return OpenAIResponseParser.extractContent(responseBody);
        }
    }

}
