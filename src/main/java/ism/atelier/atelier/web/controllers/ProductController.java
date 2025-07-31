package ism.atelier.atelier.web.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import ism.atelier.atelier.services.ProductService;
import ism.atelier.atelier.utils.mappers.ProductMapper;
import ism.atelier.atelier.web.dto.response.ProductResponseDto;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/product")
public class ProductController {
   
    @Autowired
    private ProductService productService;

    @GetMapping(produces = "application/json; charset=UTF-8")
    public List<ProductResponseDto> findAll() {
        return productService.findAll().stream()
            .map(ProductMapper::toDto)
            .collect(Collectors.toList());
    }
    @PutMapping("/{id}/stock")
    public ResponseEntity<ProductResponseDto> updateStock(
            @PathVariable Long id,
            @RequestParam Integer stock) {
        try {
            var updatedProduct = productService.updateStock(id, stock);
            return ResponseEntity.ok(ProductMapper.toDto(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{id}/availability")
    public ResponseEntity<ProductResponseDto> updateAvailability(
            @PathVariable Long id,
            @RequestParam Boolean available) {
        try {
            var updatedProduct = productService.updateAvailability(id, available);
            return ResponseEntity.ok(ProductMapper.toDto(updatedProduct));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
