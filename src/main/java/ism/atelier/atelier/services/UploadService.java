package ism.atelier.atelier.services;

import ism.atelier.atelier.data.models.Plate;
import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    Plate uploadAndCreatePlate(MultipartFile image, String name, String description, Integer price, String difficulty, String categoryId) throws Exception;
}