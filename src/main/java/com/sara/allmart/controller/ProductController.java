package com.sara.allmart.controller;

import com.sara.allmart.dto.request.ProductCategoryRequest;
import com.sara.allmart.dto.request.ProductPriceRequest;
import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.request.ProductStockRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.service.ProductService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;


@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest request){
        ProductResponse response = productService.createProduct(request);
        return new ResponseEntity<>(response,HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponse>> searchProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) @PositiveOrZero BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            @RequestParam(defaultValue = "0") @Min(0) int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<ProductResponse> response = productService.searchProducts(name, categoryId, minPrice, maxPrice, page, size);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/price")
    public ResponseEntity<ProductResponse> updatePrice(@PathVariable Long id, @Valid @RequestBody ProductPriceRequest request){
        ProductResponse response = productService.updatePrice(id, request.price());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<ProductResponse> updateStock(@PathVariable Long id,@Valid @RequestBody ProductStockRequest request){
        ProductResponse response = productService.updateStock(id, request.stock());
        return ResponseEntity.ok(response);

    }

    @PatchMapping("{id}/category")
    public ResponseEntity<ProductResponse> updateCategory(@PathVariable Long id, @Valid @RequestBody ProductCategoryRequest request){
        ProductResponse response = productService.updateCategory(id, request);
        return ResponseEntity.ok(response);

    }

    //TODO: add delete product
}
