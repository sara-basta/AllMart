package com.sara.allmart.repository;

import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    List<Order> findOrdersByUser(User user);

    Page<Order> findByUser(User user, Pageable pageable);

    @Query("SELECT COUNT(i) FROM Order o JOIN o.items i " +
            "WHERE o.user = :user " +
            "AND i.product.id = :productId " +
            "AND o.status = :status")
    long countDeliveredProductForUser(@Param("user") User user, @Param("productId") Long productId, @Param("status") OrderStatus status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.user WHERE o.id = :orderId")
    Optional<Order> findByIdWithUser(@Param("orderId") Long orderId);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.isPaid = true, o.status = :paidStatus WHERE o.id = :orderId AND o.isPaid = false")
    int markPaidIfUnpaid(@Param("orderId") Long orderId, @Param("paidStatus") OrderStatus paidStatus);
}
