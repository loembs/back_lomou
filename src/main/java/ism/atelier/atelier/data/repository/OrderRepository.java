package ism.atelier.atelier.data.repository;

import ism.atelier.atelier.data.models.Order;
import ism.atelier.atelier.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // Trouver toutes les commandes d'un utilisateur
    List<Order> findByUserOrderByOrderDateDesc(User user);

    // Trouver les commandes par statut
    List<Order> findByStatusOrderByOrderDateDesc(Order.OrderStatus status);

    // Trouver les commandes récentes (derniers 30 jours)
    @Query("SELECT o FROM Order o WHERE o.orderDate >= :startDate ORDER BY o.orderDate DESC")
    List<Order> findRecentOrders(@Param("startDate") LocalDateTime startDate);

    // Trouver une commande par numéro
    Optional<Order> findByOrderNumber(String orderNumber);

    // Compter les commandes par statut
    @Query("SELECT o.status, COUNT(o) FROM Order o GROUP BY o.status")
    List<Object[]> countOrdersByStatus();

    // Trouver les commandes avec un montant total supérieur à une valeur
    @Query("SELECT o FROM Order o WHERE o.totalAmount >= :minAmount ORDER BY o.totalAmount DESC")
    List<Order> findOrdersWithMinAmount(@Param("minAmount") java.math.BigDecimal minAmount);

    // Statistiques des ventes par période
    @Query("SELECT DATE(o.orderDate), SUM(o.totalAmount), COUNT(o) FROM Order o " +
            "WHERE o.orderDate BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> getSalesStatistics(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}