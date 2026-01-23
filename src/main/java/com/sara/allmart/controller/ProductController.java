package com.sara.allmart.controller;

import com.sara.allmart.dto.request.ProductCategoryRequest;
import com.sara.allmart.dto.request.ProductPriceRequest;
import com.sara.allmart.dto.request.ProductRequest;
import com.sara.allmart.dto.request.ProductStockRequest;
import com.sara.allmart.dto.response.ProductResponse;
import com.sara.allmart.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping("/create")
    public ProductResponse createProduct(@RequestBody ProductRequest request){
        return productService.createProduct(request);
    }

    @GetMapping
    public Page<ProductResponse> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size){
        return productService.getAllProducts(page,size);
    }

    @PutMapping("/{id}/price")
    public ProductResponse updatePrice(@PathVariable Long id, @RequestBody ProductPriceRequest request){
        return productService.updatePrice(id,request.price());
    }

    @PutMapping("/{id}/stock")
    public ProductResponse updateStock(@PathVariable Long id, @RequestBody ProductStockRequest request){
        return productService.updateStock(id,request.stock());
    }

    @PutMapping("{id}/category/update")
    public ProductResponse updateCategory(@PathVariable Long id, @RequestBody ProductCategoryRequest request){
        return productService.updateCategory(id,request);
    }
}
