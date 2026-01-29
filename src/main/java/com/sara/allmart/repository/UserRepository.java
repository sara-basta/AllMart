package com.sara.allmart.repository;

import com.sara.allmart.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u WHERE :id IS NULL OR u.id = :id")
    Page<User> searchUsers(@Param("id") Long id, Pageable pageable);
}
