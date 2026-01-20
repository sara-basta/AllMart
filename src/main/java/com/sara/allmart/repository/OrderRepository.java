package com.sara.allmart.repository;

import com.sara.allmart.entity.Order;
import com.sara.allmart.entity.OrderStatus;
import com.sara.allmart.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findOrdersByStatus(OrderStatus status);

    List<Order> findOrdersByUser(User user);

    Order findOrderById (Long Id);
}
