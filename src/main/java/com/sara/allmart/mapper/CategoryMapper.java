package com.sara.allmart.mapper;

import com.sara.allmart.dto.request.CategoryRequest;
import com.sara.allmart.dto.response.CategoryResponse;
import com.sara.allmart.entity.Category;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class CategoryMapper {

    public Category toEntity(CategoryRequest request){
        Category category = new Category();
        category.setName(request.name());
        category.setDescription(request.description());
        category.setProducts(new ArrayList<>());
        return category;
    }

    public CategoryResponse toResponse(Category category){
        Long id = category.getId();
        String name = category.getName();
        String description = category.getDescription();
        return new CategoryResponse(id,name,description);
    }
}
