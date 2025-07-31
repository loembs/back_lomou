package ism.atelier.atelier.services;

import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.services.impl.WebSocketServiceImpl;
import org.springframework.messaging.simp.SimpMessagingTemplate;

public interface WebSocketService {

    public void notifyProductUpdate(Product product);

    public void notifyProductStockChange(Long productId, Integer newStock);

    public void notifyProductAvailabilityChange(Long productId, Boolean available);

    public void notifyCacheInvalidation();


}
