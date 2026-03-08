package com.sara.allmart.repository;

import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrdersByUser(User user);

    List<Order> findByUserOrderByCreatedAtDesc(User user);

    @Query("SELECT COUNT(i) FROM Order o JOIN o.items i " +
            "WHERE o.user = :user " +
            "AND i.product.id = :productId " +
            "AND o.status = :status")
    long countDeliveredProductForUser(@Param("user") User user, @Param("productId") Long productId, @Param("status") OrderStatus status);
}
