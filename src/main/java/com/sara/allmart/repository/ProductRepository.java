package com.sara.allmart.repository;

import com.sara.allmart.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByDeletedFalse(Pageable pageable);

    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);

    Page<Product> findByDeletedFalseOrderByAverageRatingDesc(Pageable pageable);

    Page<Product> findByDeletedFalseAndNameContainingIgnoreCase(String name, Pageable pageable);

    @Query(value = "SELECT * FROM products p WHERE " +
            "(:name IS NULL OR CAST(p.name AS TEXT) ILIKE CAST('%' || :name || '%' AS TEXT)) AND " +
            "(:categoryId IS NULL OR p.category_id = :categoryId) AND " +
            "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
            "(:maxPrice IS NULL OR p.price <= :maxPrice) AND "+
            "(:includeDeleted = TRUE OR p.is_deleted = FALSE)",
            nativeQuery = true)
    Page<Product> searchProducts(
            @Param("name") String name,
            @Param("categoryId") Long categoryId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice,
            @Param("includeDeleted") boolean includeDeleted,
            Pageable pageable
    );
}
