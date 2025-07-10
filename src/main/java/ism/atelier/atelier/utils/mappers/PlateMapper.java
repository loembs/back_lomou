package ism.atelier.atelier.utils.mappers;

import ism.atelier.atelier.data.models.Plate;
import ism.atelier.atelier.web.dto.response.PlateResponseDto;
import ism.atelier.atelier.web.dto.request.PlateRequestDto;

public class PlateMapper {
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
        // Bloc supprimé car PlateResponseDto n'a pas ces setters
        return dto;
    }

    public static Plate toEntity(PlateRequestDto dto) {
        Plate plate = new Plate();
        plate.setName(dto.getName());
        plate.setDescription(dto.getDescription());
        plate.setPrice(dto.getPrice());
        plate.setImageUrl(dto.getImageUrl());
        plate.setModel3dUrl(dto.getModel3dUrl());
        plate.setModelUsdzUrl(dto.getModelUsdzUrl());
        plate.setHasAr(dto.getHasAr());
        plate.setDifficulty(dto.getDifficulty());
        // La catégorie doit être associée dans le service
        return plate;
    }
} 