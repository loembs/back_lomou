package ism.atelier.atelier.utils.mappers;
import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.web.dto.request.ProductRequestDto;
import ism.atelier.atelier.web.dto.response.ProductResponseDto;

public class ProductMapper {
    public static ProductResponseDto toDto(Product product){
        ProductResponseDto dto=new ProductResponseDto(); 

        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setImageUrl(product.getImageUrl());
        dto.setPrice(product.getPrice());
        dto.setDescription(product.getDescription());

        return dto;
    }

    public static Product toEntity(ProductRequestDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setImageUrl(dto.getImageUrl());
       
        return  product;
    }
}
