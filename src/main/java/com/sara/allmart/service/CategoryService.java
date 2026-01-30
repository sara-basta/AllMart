package com.sara.allmart.service;

import com.sara.allmart.dto.request.CategoryRequest;
import com.sara.allmart.dto.request.UpdateCategoryRequest;
import com.sara.allmart.dto.response.CategoryResponse;
import com.sara.allmart.entity.Category;
import com.sara.allmart.exception.ResourceNotFoundException;
import com.sara.allmart.mapper.CategoryMapper;
import com.sara.allmart.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public CategoryResponse createCategory(CategoryRequest request){
        Category category = categoryMapper.toEntity(request);
        categoryRepository.save(category);
        return categoryMapper.toResponse(category);
    }

    public Page<CategoryResponse> getAllCategories(Long id,int page, int size) {
        Pageable pageable = PageRequest.of(page,size);
        Page<Category> categoryPage = categoryRepository.getCategories(id,pageable);
        return categoryPage.map(categoryMapper::toResponse);
    }

    public void deleteCategory(Long categoryId) {
        if(!categoryRepository.existsById(categoryId)){
            throw new ResourceNotFoundException("Category not found!");
        }
        categoryRepository.deleteById(categoryId);
    }

    public CategoryResponse updateCategory(Long categoryId, UpdateCategoryRequest request) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        if(request.name()!=null && !request.name().isBlank()){
            category.setName(request.name());
        }
        if(request.description()!=null && !request.description().isBlank()){
            category.setDescription(request.description());
        }

        return categoryMapper.toResponse(categoryRepository.save(category));
    }
}
