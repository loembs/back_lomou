package ism.atelier.atelier.services.impl;

import ism.atelier.atelier.data.models.Order;
import ism.atelier.atelier.data.models.User;
import ism.atelier.atelier.data.models.Product;
import ism.atelier.atelier.data.models.OrderItem;
import ism.atelier.atelier.data.repository.OrderRepository;
import ism.atelier.atelier.data.repository.ProductRepository;
import ism.atelier.atelier.services.OrderService;
import ism.atelier.atelier.web.dto.request.CreateOrderRequestDto;
import ism.atelier.atelier.web.dto.request.OrderItemRequestDto;
import ism.atelier.atelier.web.dto.response.OrderResponseDto;
import ism.atelier.atelier.web.dto.response.OrderStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto request, User user) {
        log.info("Création d'une nouvelle commande pour l'utilisateur: {}", user.getEmail());

        // Créer la commande
        Order order = new Order();
        order.setUser(user);
        order.setStatus(Order.OrderStatus.PENDING);
        order.setShippingAddress(request.getShippingAddress());
        order.setPhoneNumber(request.getPhoneNumber());
        order.setCustomerName(request.getCustomerName());
        order.setNotes(request.getNotes());

        // Créer les éléments de commande
        List<OrderItem> orderItems = request.getItems().stream()
                .map(itemRequest -> {
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new RuntimeException("Produit non trouvé: " + itemRequest.getProductId()));
                    // Vérifier le stock
                    if (product.getStock() < itemRequest.getQuantity()) {
                        throw new RuntimeException("Stock insuffisant pour le produit: " + product.getName());
                    }

                    // Créer l'élément de commande
                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(order);
                    orderItem.setProduct(product);
                    orderItem.setQuantity(itemRequest.getQuantity());
                    orderItem.setUnitPrice(BigDecimal.valueOf(product.getPrice()));

                    // Mettre à jour le stock
                    product.setStock(product.getStock() - itemRequest.getQuantity());
                    productRepository.save(product);

                    log.info("Ajout du produit {} (quantité: {}) à la commande", product.getName(), itemRequest.getQuantity());

                    return orderItem;
                })
                .collect(Collectors.toList());

        // Calculer le montant total
        BigDecimal totalAmount = orderItems.stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalAmount(totalAmount);
        order.setOrderItems(orderItems);

        Order savedOrder = orderRepository.save(order);
        log.info("Commande créée avec succès. Numéro: {}, Montant total: {}", savedOrder.getOrderNumber(), totalAmount);

        return mapToOrderResponseDto(savedOrder);
    }

    @Override
    public List<OrderResponseDto> getUserOrders(User user) {
        log.info("Récupération des commandes pour l'utilisateur: {}", user.getEmail());

        return orderRepository.findByUserOrderByOrderDateDesc(user)
                .stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderResponseDto> getAllOrders() {
        log.info("Récupération de toutes les commandes");

        return orderRepository.findAll()
                .stream()
                .map(this::mapToOrderResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrderResponseDto getOrderById(Long orderId) {
        log.info("Récupération de la commande avec l'ID: {}", orderId);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + orderId));

        return mapToOrderResponseDto(order);
    }

    @Override
    @Transactional
    public OrderResponseDto updateOrderStatus(Long orderId, Order.OrderStatus newStatus) {
        log.info("Mise à jour du statut de la commande {} vers: {}", orderId, newStatus);

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée avec l'ID: " + orderId));

        order.setStatus(newStatus);

        // Mettre à jour les dates selon le statut
        if (newStatus == Order.OrderStatus.SHIPPED && order.getShippedDate() == null) {
            order.setShippedDate(LocalDateTime.now());
            log.info("Date d'expédition mise à jour pour la commande: {}", orderId);
        } else if (newStatus == Order.OrderStatus.DELIVERED && order.getDeliveredDate() == null) {
            order.setDeliveredDate(LocalDateTime.now());
            log.info("Date de livraison mise à jour pour la commande: {}", orderId);
        }

        Order updatedOrder = orderRepository.save(order);
        log.info("Statut de la commande {} mis à jour avec succès vers: {}", orderId, newStatus);

        return mapToOrderResponseDto(updatedOrder);
    }

    @Override
    public OrderStatisticsResponseDto getOrderStatistics() {
        log.info("Calcul des statistiques des commandes");

        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);

        // Commandes récentes
        List<Order> recentOrders = orderRepository.findRecentOrders(thirtyDaysAgo);

        // Statistiques par statut
        List<Object[]> statusCounts = orderRepository.countOrdersByStatus();

        // Calculer les statistiques
        long totalOrders = recentOrders.size();
        BigDecimal totalRevenue = recentOrders.stream()
                .map(Order::getTotalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        log.info("Statistiques calculées - Total commandes: {}, Revenus: {}", totalOrders, totalRevenue);

        return OrderStatisticsResponseDto.builder()
                .totalOrders(totalOrders)
                .totalRevenue(totalRevenue)
                .recentOrders(recentOrders.stream()
                        .map(this::mapToOrderResponseDto)
                        .collect(Collectors.toList()))
                .statusCounts(statusCounts.stream()
                        .collect(Collectors.toMap(
                                row -> ((Order.OrderStatus) row[0]).name(),
                                row -> (Long) row[1]
                        )))
                .build();
    }

    /**
     * Mapper une entité Order vers OrderResponseDto
     */
    private OrderResponseDto mapToOrderResponseDto(Order order) {
        return OrderResponseDto.builder()
                .id(order.getId())
                .orderNumber(order.getOrderNumber())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .orderDate(order.getOrderDate())
                .shippedDate(order.getShippedDate())
                .deliveredDate(order.getDeliveredDate())
                .shippingAddress(order.getShippingAddress())
                .phoneNumber(order.getPhoneNumber())
                .customerName(order.getCustomerName())
                .notes(order.getNotes())
                .customerEmail(order.getUser().getEmail())
                .items(order.getOrderItems().stream()
                        .map(this::mapToOrderItemResponseDto)
                        .collect(Collectors.toList()))
                .build();
    }

    /**
     * Mapper une entité OrderItem vers OrderItemResponseDto
     */
    private OrderResponseDto.OrderItemResponseDto mapToOrderItemResponseDto(OrderItem orderItem) {
        return OrderResponseDto.OrderItemResponseDto.builder()
                .id(orderItem.getId())
                .productId(orderItem.getProduct().getId())
                .productName(orderItem.getProduct().getName())
                .quantity(orderItem.getQuantity())
                .unitPrice(orderItem.getUnitPrice())
                .totalPrice(orderItem.getTotalPrice())
                .build();
    }
}