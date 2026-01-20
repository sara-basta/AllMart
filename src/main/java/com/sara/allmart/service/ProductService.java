package com.sara.allmart.service;

import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Product;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.ProductMapper;
import com.sara.allmart.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    public ProductResponse createProduct(String name, String description, BigDecimal price, Integer stock) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStockQuantity(stock);
        productRepository.save(product);

        return productMapper.toResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse updatePrice(Long id, BigDecimal newPrice) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        product.setPrice(newPrice);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }

    public ProductResponse updateStock(Long id, Integer newStock) {
        Product product = productRepository.findProductById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found!"));
        product.setStockQuantity(newStock);
        productRepository.save(product);
        return productMapper.toResponse(product);
    }
}
