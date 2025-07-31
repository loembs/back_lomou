package ism.atelier.atelier.services.impl;

import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.services.WebSocketService;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class WebSocketServiceImpl implements WebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public WebSocketServiceImpl(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void notifyProductUpdate(Product product) {
        messagingTemplate.convertAndSend("/topic/products/update", product);
    }

    public void notifyProductStockChange(Long productId, Integer newStock) {
        messagingTemplate.convertAndSend("/topic/products/stock",
                new StockUpdateMessage(productId, newStock));
    }

    public void notifyProductAvailabilityChange(Long productId, Boolean available) {
        messagingTemplate.convertAndSend("/topic/products/availability",
                new AvailabilityUpdateMessage(productId, available));
    }

    public void notifyCacheInvalidation() {
        messagingTemplate.convertAndSend("/topic/cache/invalidate", "refresh");
    }

    // Classes internes pour les messages
    public static class StockUpdateMessage {
        private Long productId;
        private Integer stock;

        public StockUpdateMessage(Long productId, Integer stock) {
            this.productId = productId;
            this.stock = stock;
        }

        // Getters et setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Integer getStock() { return stock; }
        public void setStock(Integer stock) { this.stock = stock; }
    }

    public static class AvailabilityUpdateMessage {
        private Long productId;
        private Boolean available;

        public AvailabilityUpdateMessage(Long productId, Boolean available) {
            this.productId = productId;
            this.available = available;
        }

        // Getters et setters
        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }
        public Boolean getAvailable() { return available; }
        public void setAvailable(Boolean available) { this.available = available; }
    }
}