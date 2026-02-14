package com.sara.allmart.repository;

import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrdersByUser(User user);

    List<Order> findByUser(User user);

    @Query("SELECT COUNT(o) > 0 FROM Order o JOIN o.items i " +
            "WHERE o.user = :user " +
            "AND i.product.id = :productId " +
            "AND o.status = 'DELIVERED'")
    boolean hasUserPurchasedProduct(@Param("user") User user, @Param("productId") Long productId);
}
