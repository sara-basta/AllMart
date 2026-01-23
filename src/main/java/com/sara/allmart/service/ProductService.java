package com.sara.allmart.service;

import com.sara.allmart.dto.request.ProductCategoryRequest;
import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Category;
import com.sara.allmart.entity.Product;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.ProductMapper;
import com.sara.allmart.repository.CategoryRepository;
import com.sara.allmart.repository.ProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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

    public ProductResponse createProduct(ProductRequest request) {
        Product product = productMapper.toEntity(request);
        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    public Page<ProductResponse> getAllProducts(int page, int size) {
        return productRepository.findAll(PageRequest.of(page,size))
                .map(productMapper::toResponse);
    }

    public ProductResponse updatePrice(Long id, BigDecimal newPrice) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        product.setPrice(newPrice);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse updateStock(Long id, Integer newStock) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        product.setStockQuantity(newStock);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse updateCategory(Long id, ProductCategoryRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        product.setCategory(category);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }
}
