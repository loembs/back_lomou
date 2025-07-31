package ism.atelier.atelier.services;
import ism.atelier.atelier.data.models.*;
import  java.util.List;

public interface ProductService {

    List<Product> findAll();
    Product save(Product product);
    Product updateStock(Long productId, Integer newStock);
    Product updateAvailability(Long productId, Boolean available);

}