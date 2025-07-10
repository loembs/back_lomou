package ism.atelier.atelier.utils.mappers;

import ism.atelier.atelier.data.models.Category;
import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.web.dto.response.CategoryResponseDto;
import ism.atelier.atelier.web.dto.response.PlateResponseDto;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryResponseDto toDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        if (category.getPlates() != null) {
            dto.setPlates(category.getPlates().stream().map(CategoryMapper::toDto).collect(Collectors.toList()));
        }
        return dto;
    }

    public static PlateResponseDto toDto(Plate plate) {
        PlateResponseDto dto = new PlateResponseDto();
        dto.setId(plate.getId());
        dto.setName(plate.getName());
        dto.setDescription(plate.getDescription());
        dto.setPrice(plate.getPrice());
        dto.setImageUrl(plate.getImageUrl());
        dto.setModel3dUrl(plate.getModel3dUrl());
        dto.setModelUsdzUrl(plate.getModelUsdzUrl());
        dto.setHasAr(plate.getHasAr());
        dto.setDifficulty(plate.getDifficulty());
        return dto;
    }
} 