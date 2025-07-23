package ism.atelier.atelier.web.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class CategoryResponseDto {
    private String id;
    private String name;
    private List<ProductResponseDto> plates;
} 