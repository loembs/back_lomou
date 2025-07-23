package ism.atelier.atelier.utils.mappers;

import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.web.dto.response.CategoryResponseDto;
import ism.atelier.atelier.web.dto.response.PlateResponseDto;
import ism.atelier.atelier.web.dto.response.ProductResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryResponseDto toDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getProducts()!= null) {
            dto.setPlates(category.getProducts().stream().map(CategoryMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static ProductResponseDto toDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setImageUrl(product.getImageUrl());
       
        return dto;
    }
} 