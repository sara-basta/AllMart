package com.sara.allmart.controller;

import com.sara.allmart.dto.request.CategoryRequest;
import com.sara.allmart.dto.response.CategoryResponse;
import com.sara.allmart.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/categories")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/create")
    public CategoryResponse createCategory(@RequestBody CategoryRequest request){
        return categoryService.createCategory(request);
    }

    @GetMapping
    public Page<CategoryResponse> getAllCategories(@RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "20") int size){
        return categoryService.getAllCategories(page,size);
    }

    @DeleteMapping("/{categoryId}/delete")
    public void deleteCategory(@PathVariable Long categoryId){
        categoryService.deleteCategory(categoryId);
    }
}
