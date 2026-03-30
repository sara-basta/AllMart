package com.sara.allmart.service;

import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Category;
import com.sara.allmart.entity.Product;
import com.sara.allmart.entity.ProductImage;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.ProductMapper;
import com.sara.allmart.repository.CategoryRepository;
import com.sara.allmart.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.categoryRepository = categoryRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    @Cacheable(value = "products")
    public Page<ProductResponse> searchProducts(String name, Long categoryId, BigDecimal minPrice, BigDecimal maxPrice,boolean isDeleted, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Product> productPage = productRepository.searchProducts(name, categoryId, minPrice, maxPrice,isDeleted, pageable);
        return productPage.map(productMapper::toResponse);
    }

    // soft-delete
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse deleteProduct(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));

        product.setDeleted(true);
        product.setStockQuantity(0);
        return productMapper.toResponse(productRepository.save(product));
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        if (product.isDeleted()) {
            throw new ResourceNotFoundException("Product not found!");
        }
        return productMapper.toResponse(product);
    }

    public ProductResponse getAdminProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        return productMapper.toResponse(product);
    }

    @Transactional
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + id));

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with ID: " + request.categoryId()));

        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());

        if (request.imageUrls() != null) {
            product.getImages().clear();
            List<String> uniqueUrls = new LinkedHashSet<>(request.imageUrls()).stream().toList();
            for (int i = 0; i < uniqueUrls.size(); i++) {
                ProductImage img = ProductImage.builder()
                        .imageUrl(uniqueUrls.get(i))
                        .position(i)
                        .build();
                product.addImage(img);
            }
        }
        product.setCategory(category);

        return productMapper.toResponse(productRepository.save(product));
    }
}
