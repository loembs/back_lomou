package ism.atelier.atelier.services;

import java.io.IOException;

public interface MeshyApiService {
    String generateModelFromImage(String imageUrl) throws IOException;
}