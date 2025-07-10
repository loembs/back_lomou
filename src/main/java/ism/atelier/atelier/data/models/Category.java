package ism.atelier.atelier.data.models;

import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Data
public class Category {
    @Id
    private String id;
    private String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Plate> plates;
}