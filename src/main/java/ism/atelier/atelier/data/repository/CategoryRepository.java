package ism.atelier.atelier.data.repository;

import ism.atelier.atelier.data.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, String> {
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.products")
    List<Category> findAllWithPlates();
} 