package com.example.payment;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;

public class PaymentTester {
    public static void main(String[] args) throws Exception {
        // Read the JSON file
        String json = Files.readString(Path.of("C:\\Users\\kalin\\OneDrive\\OneDrive Documents\\Uni Stuff\\Y2\\S2 IN2033 Team Project\\IN2033-Team-Project\\FX\\src\\main\\java\\com\\example\\payment\\test_payment.json"));

        // Send the request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/payments/accept"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = client.send(request,
                HttpResponse.BodyHandlers.ofString());

        System.out.println("Status: " + response.statusCode());
        System.out.println("Response: " + response.body());
    }
}
