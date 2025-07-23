package ism.atelier.atelier.data.repository;
import ism.atelier.atelier.data.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long>{
    
}
