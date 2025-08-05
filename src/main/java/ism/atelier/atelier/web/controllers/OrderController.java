package ism.atelier.atelier.web.controllers;

import ism.atelier.atelier.data.models.Order;
import ism.atelier.atelier.data.models.User;
import ism.atelier.atelier.services.OrderService;
import ism.atelier.atelier.services.UserService;
import ism.atelier.atelier.web.dto.request.CreateOrderRequestDto;
import ism.atelier.atelier.web.dto.response.OrderResponseDto;
import ism.atelier.atelier.web.dto.response.OrderStatisticsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = {
        "https://loumo-frontend.vercel.app",
        "http://localhost:5173",
        "http://localhost:3000",
        "http://localhost:8080"
}, allowedHeaders = "*", methods = {
        RequestMethod.GET,
        RequestMethod.POST,
        RequestMethod.PUT,
        RequestMethod.DELETE,
        RequestMethod.OPTIONS,
        RequestMethod.HEAD,
        RequestMethod.PATCH
})
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final UserService userService;

    // Endpoint pour créer une commande (CLIENT)
    @PostMapping
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody CreateOrderRequestDto request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        OrderResponseDto order = orderService.createOrder(request, user);
        return ResponseEntity.ok(order);
    }

    // Endpoint pour obtenir les commandes de l'utilisateur connecté (CLIENT)
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDto>> getMyOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        List<OrderResponseDto> orders = orderService.getUserOrders(user);
        return ResponseEntity.ok(orders);
    }

    // Endpoint pour obtenir une commande spécifique (CLIENT - seulement ses propres commandes)
    @GetMapping("/my-orders/{orderId}")
    public ResponseEntity<OrderResponseDto> getMyOrder(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        OrderResponseDto order = orderService.getOrderById(orderId);

        // Vérifier que la commande appartient à l'utilisateur connecté
        if (!order.getCustomerEmail().equals(user.getEmail())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(order);
    }

    // Endpoint pour obtenir toutes les commandes (ADMIN seulement)
    @GetMapping("/admin/all")
    public ResponseEntity<List<OrderResponseDto>> getAllOrders() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Vérifier que l'utilisateur est ADMIN
        if (!user.getRole().equals(User.Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        List<OrderResponseDto> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    // Endpoint pour obtenir une commande spécifique (ADMIN seulement)
    @GetMapping("/admin/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderById(@PathVariable Long orderId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Vérifier que l'utilisateur est ADMIN
        if (!user.getRole().equals(User.Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        OrderResponseDto order = orderService.getOrderById(orderId);
        return ResponseEntity.ok(order);
    }

    // Endpoint pour mettre à jour le statut d'une commande (ADMIN seulement)
    @PutMapping("/admin/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestParam Order.OrderStatus status) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Vérifier que l'utilisateur est ADMIN
        if (!user.getRole().equals(User.Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        OrderResponseDto updatedOrder = orderService.updateOrderStatus(orderId, status);
        return ResponseEntity.ok(updatedOrder);
    }

    // Endpoint pour obtenir les statistiques des commandes (ADMIN seulement)
    @GetMapping("/admin/statistics")
    public ResponseEntity<OrderStatisticsResponseDto> getOrderStatistics() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();

        // Vérifier que l'utilisateur est ADMIN
        if (!user.getRole().equals(User.Role.ADMIN)) {
            return ResponseEntity.status(403).build();
        }

        OrderStatisticsResponseDto statistics = orderService.getOrderStatistics();
        return ResponseEntity.ok(statistics);
    }
}