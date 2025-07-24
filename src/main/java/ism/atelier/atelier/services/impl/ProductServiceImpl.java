package ism.atelier.atelier.services.impl;

import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.data.repository.ProductRepository;
import ism.atelier.atelier.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
        
    }
}
