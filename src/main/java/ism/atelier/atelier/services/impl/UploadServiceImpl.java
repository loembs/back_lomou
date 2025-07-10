package ism.atelier.atelier.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.data.repository.CategoryRepository;
import ism.atelier.atelier.data.repository.PlateRepository;
import ism.atelier.atelier.services.UploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
public class UploadServiceImpl implements UploadService {
    @Autowired
    private Cloudinary cloudinary;
    @Autowired
    private MeshyApiService meshyApiService;
    @Autowired
    private CategoryRepository categoryRepo;
    @Autowired
    private PlateRepository plateRepo;

    public Plate uploadAndCreatePlate(MultipartFile image, String name, String description, Integer price, String difficulty, String categoryId) throws Exception {
        // Upload image to Cloudinary
        Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = uploadResult.get("secure_url").toString();

        // Call Meshy API to generate model
        String glbUrl = meshyApiService.generateModelFromImage(imageUrl);

        // Create and save Plate
        Plate plate = new Plate();
        plate.setName(name);
        plate.setDescription(description);
        plate.setPrice(price);
        plate.setImageUrl(imageUrl);
        plate.setModel3dUrl(glbUrl);
        plate.setHasAr(true);
        plate.setDifficulty(difficulty);

        Category category = categoryRepo.findById(categoryId).orElseThrow();
        plate.setCategory(category);

        return plateRepo.save(plate);
    }
} 