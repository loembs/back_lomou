package ism.atelier.atelier.data.models;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Product {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imageUrl;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    
}
