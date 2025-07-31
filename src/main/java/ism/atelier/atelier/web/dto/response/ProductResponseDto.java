package ism.atelier.atelier.web.dto.response;

import lombok.Data;

@Data
public class ProductResponseDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
    private Integer stock;
    private Boolean available;
    private Long categoryId;
    
}
