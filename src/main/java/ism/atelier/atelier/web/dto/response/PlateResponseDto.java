package ism.atelier.atelier.web.dto.response;

import lombok.Data;

@Data
public class PlateResponseDto {
    private Long id;
    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private String model3dUrl;
    private String modelUsdzUrl;
    private Boolean hasAr;
    private String difficulty;
}