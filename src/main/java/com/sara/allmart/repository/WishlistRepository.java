package com.sara.allmart.repository;

import com.sara.allmart.entity.User;
import com.sara.allmart.entity.Wishlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface WishlistRepository extends JpaRepository<Wishlist,Long> {
    Page<Wishlist> findByUser(User user, Pageable pageable);
    void deleteByUserAndProduct_Id(User user, Long productId);
    boolean existsByUserAndProduct_Id(User user,Long productId);
}
