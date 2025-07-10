// Assurez-vous d'avoir la dépendance javax.persistence dans votre pom.xml
package ism.atelier.atelier.data.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Plate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer price;
    private String imageUrl;
    private String model3dUrl;
    private String modelUsdzUrl;
    private Boolean hasAr;
    private String difficulty;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
} 