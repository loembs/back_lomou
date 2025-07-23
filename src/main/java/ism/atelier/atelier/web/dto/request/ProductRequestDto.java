package ism.atelier.atelier.web.dto.request;
import lombok.Data;


@Data
public class ProductRequestDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;
}
