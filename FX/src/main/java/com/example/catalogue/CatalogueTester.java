package com.example.catalogue;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class CatalogueTester {
    public static void main(String[] args) throws Exception {
        InputStream inputStream = CatalogueTester.class.getResourceAsStream("/com/example/fx/test_sales.json");
        if (inputStream == null) {
            throw new RuntimeException("Resource not found: test_sales.json");
        }
        String json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/catalogue/accept"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }
}
