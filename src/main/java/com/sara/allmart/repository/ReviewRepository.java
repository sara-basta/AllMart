package com.sara.allmart.repository;

import com.sara.allmart.entity.Review;
import com.sara.allmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review,Long> {
    Page<Review> findByProduct_Id(Long productId, Pageable pageable);
    boolean existsByUserAndProduct_Id(User user, Long productId);
}
