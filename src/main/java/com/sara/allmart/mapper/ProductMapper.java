package com.sara.allmart.mapper;

import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Category;
import com.sara.allmart.entity.Product;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.repository.CategoryRepository;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {
    private final CategoryRepository categoryRepository;

    public ProductMapper(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Product toEntity(ProductRequest request){
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setCategory(category);

        return product;
    }

    public ProductResponse toResponse(Product product){
        Long id = product.getId();
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        Integer stock = product.getStockQuantity();
        String categoryName = "Uncategorized";
        String imageUrl = product.getImageUrl();
        int reviewCount = product.getReviewCount();
        double averageRating = product.getAverageRating();
        if(product.getCategory() != null) {
        categoryName = product.getCategory().getName();
        }
        return new ProductResponse(id,name,description,price,stock,categoryName,imageUrl,reviewCount,averageRating);
    }
}
