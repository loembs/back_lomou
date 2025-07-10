package ism.atelier.atelier.services.impl;

import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class MeshyApiService {
    @Value("${meshy.api_key}")
    private String apiKey;

    public String generateModelFromImage(String imageUrl) throws IOException {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");

        String json = "{\"image_url\": \"" + imageUrl + "\", \"output_format\": \"glb\"}";

        Request request = new Request.Builder()
            .url("https://api.meshy.ai/v1/image-to-3d")
            .post(RequestBody.create(json, mediaType))
            .addHeader("Authorization", "Bearer " + apiKey)
            .build();

        try (Response response = client.newCall(request).execute()) {
            // Extraire l'URL du modèle depuis la réponse JSON
            String responseBody = response.body().string();
            // À adapter selon la réponse réelle de Meshy
            return "https://meshy.ai/output-model.glb"; // Placeholder
        }
    }
} 