package com.example.catalogue;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class CatalogueTester {
    public static void loadSampleCatalogue() {
        try {
            InputStream inputStream = CatalogueTester.class.getResourceAsStream("/com/example/fx/CAStock.json");
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

            HttpResponse<String> response = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofString()
            );

            System.out.println("Catalogue load status: " + response.statusCode());
            System.out.println("Catalogue load response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        loadSampleCatalogue();
    }
}
