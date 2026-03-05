package com.sara.allmart.repository;

import com.sara.allmart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteByCartUserEmail(String email);
}