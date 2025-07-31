package ism.atelier.atelier.services.impl;

import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.data.repository.ProductRepository;
import ism.atelier.atelier.services.ProductService;
import ism.atelier.atelier.services.WebSocketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService{
    private final ProductRepository productRepository;
    private final WebSocketService webSocketService;

    @Override
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Override
    public Product save(Product product) {
        Product savedProduct = productRepository.save(product);
        webSocketService.notifyProductUpdate(savedProduct);
        return savedProduct;
    }

    @Override
    public Product updateStock(Long productId, Integer newStock) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setStock(newStock);
            product.setAvailable(newStock > 0);
            Product updatedProduct = productRepository.save(product);

            // Notifier via WebSocket
            webSocketService.notifyProductStockChange(productId, newStock);
            webSocketService.notifyProductAvailabilityChange(productId, newStock > 0);
            webSocketService.notifyCacheInvalidation();

            return updatedProduct;
        }
        throw new RuntimeException("Produit non trouvé avec l'ID: " + productId);
    }

    @Override
    public Product updateAvailability(Long productId, Boolean available) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setAvailable(available);
            Product updatedProduct = productRepository.save(product);

            // Notifier via WebSocket
            webSocketService.notifyProductAvailabilityChange(productId, available);
            webSocketService.notifyCacheInvalidation();

            return updatedProduct;
        }
        throw new RuntimeException("Produit non trouvé avec l'ID: " + productId);
    }
}