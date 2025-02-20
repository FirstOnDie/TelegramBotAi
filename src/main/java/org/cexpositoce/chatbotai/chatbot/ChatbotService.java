package org.cexpositoce.chatbotai.chatbot;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class ChatbotService {

    @Value("${huggingface.api-key}")
    private String apiKey;

    @Value("${huggingface.model}")
    private String model;

    public String generarRespuesta(String mensaje) {
        try {
            String requestBody = """
                {
                    "inputs": "%s"
                }
                """.formatted(mensaje);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api-inference.huggingface.co/models/" + model))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString("{\"inputs\": \"" + mensaje + "\"}"))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            return "Error al conectar con Hugging Face: " + e.getMessage();
        }
    }

}