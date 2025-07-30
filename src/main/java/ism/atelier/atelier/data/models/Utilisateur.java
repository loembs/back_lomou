package ism.atelier.atelier.data.models;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "utilisateur")

@Data
public class Utilisateur extends Personne {
     
}
