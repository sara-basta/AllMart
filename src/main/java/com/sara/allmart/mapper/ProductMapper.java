package com.sara.allmart.mapper;

import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.entity.Product;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product){
        String name = product.getName();
        String description = product.getDescription();
        BigDecimal price = product.getPrice();
        Integer stock = product.getStockQuantity();

        return new ProductResponse(name,description,price,stock);
    }
}
