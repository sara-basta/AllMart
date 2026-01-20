package com.sara.allmart.mapper;

import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public Product toEntity(ProductRequest request){
        Product product = new Product();
        product.setName(request.name());
        product.setDescription(request.description());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        return product;
    }

    public ProductResponse toResponse(Product product){
        Long id = product.getId();
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        Integer stock = product.getStockQuantity();

        return new ProductResponse(id,name,description,price,stock);
    }
}
