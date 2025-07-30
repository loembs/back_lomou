package ism.atelier.atelier.data.models;

import org.springframework.data.annotation.Id;
import lombok.Data;

@Data
public class Personne {
    @Id
    private String id;
    private String nomComplet;
    private String image;
    
}
