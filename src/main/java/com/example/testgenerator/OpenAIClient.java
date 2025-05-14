package com.example.testgenerator;

import java.io.IOException;
import okhttp3.*;
import org.json.JSONObject;

public class OpenAIClient {

    private static final String API_KEY = "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";
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

}
