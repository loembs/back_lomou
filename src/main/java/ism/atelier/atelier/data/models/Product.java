package ism.atelier.atelier.data.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;


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
        private Integer stock = 0; // Stock disponible
        private Boolean available = true; // Disponibilité
        private LocalDateTime lastModified = LocalDateTime.now(); // Pour le cache

        @ManyToOne
        @JoinColumn(name = "category_id")
        private Category category;

    }
