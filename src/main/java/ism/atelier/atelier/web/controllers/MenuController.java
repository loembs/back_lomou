package ism.atelier.atelier.web.controllers;

import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.services.MenuService;
import ism.atelier.atelier.services.UploadService;
import ism.atelier.atelier.utils.mappers.CategoryMapper;
import ism.atelier.atelier.web.dto.response.CategoryResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @Autowired
    private UploadService uploadService;

    @GetMapping
    public List<CategoryResponseDto> getMenu() {
        return menuService.getMenu().stream()
            .map(CategoryMapper::toDto)
            .collect(Collectors.toList());
    }

    @PostMapping("/plate")
    public ResponseEntity<Plate> addPlate(@RequestBody Plate plate) {
        return ResponseEntity.ok(menuService.savePlate(plate));
    }

    @PostMapping("/plate/upload")
    public ResponseEntity<Plate> uploadAndCreatePlate(@RequestParam("image") MultipartFile image,
                                                      @RequestParam("name") String name,
                                                      @RequestParam("description") String description,
                                                      @RequestParam("price") Integer price,
                                                      @RequestParam("difficulty") String difficulty,
                                                      @RequestParam("categoryId") String categoryId) throws Exception {
        Plate plate = uploadService.uploadAndCreatePlate(image, name, description, price, difficulty, categoryId);
        return ResponseEntity.ok(plate);
    }
} 