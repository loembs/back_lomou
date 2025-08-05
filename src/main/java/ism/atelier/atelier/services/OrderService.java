package ism.atelier.atelier.services;

import ism.atelier.atelier.data.models.Order;
import ism.atelier.atelier.data.models.User;
import ism.atelier.atelier.web.dto.request.CreateOrderRequestDto;
import ism.atelier.atelier.web.dto.response.OrderResponseDto;
import ism.atelier.atelier.web.dto.response.OrderStatisticsResponseDto;

import java.util.List;

public interface OrderService {

    /**
     * Créer une nouvelle commande
     * @param request DTO contenant les informations de la commande
     * @param user Utilisateur qui passe la commande
     * @return DTO de la commande créée
     */
    OrderResponseDto createOrder(CreateOrderRequestDto request, User user);

    /**
     * Obtenir toutes les commandes d'un utilisateur
     * @param user Utilisateur dont on veut les commandes
     * @return Liste des commandes de l'utilisateur
     */
    List<OrderResponseDto> getUserOrders(User user);

    /**
     * Obtenir toutes les commandes (pour les admins)
     * @return Liste de toutes les commandes
     */
    List<OrderResponseDto> getAllOrders();

    /**
     * Obtenir une commande par son ID
     * @param orderId ID de la commande
     * @return DTO de la commande
     */
    OrderResponseDto getOrderById(Long orderId);

    /**
     * Mettre à jour le statut d'une commande
     * @param orderId ID de la commande
     * @param newStatus Nouveau statut
     * @return DTO de la commande mise à jour
     */
    OrderResponseDto updateOrderStatus(Long orderId, Order.OrderStatus newStatus);

    /**
     * Obtenir les statistiques des commandes (pour les admins)
     * @return DTO contenant les statistiques
     */
    OrderStatisticsResponseDto getOrderStatistics();
}