package com.dev.blog.service.impl;

import com.dev.blog.domain.entities.Category;
import com.dev.blog.repository.CategoryRepository;
import com.dev.blog.service.CategoryService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        log.info( "Get all categories from service" );
        return categoryRepository.findAllWithPostCount();
    }

    @Override
    @Transactional
    public Category createCategory(Category category) {
        if(categoryRepository.existsByName(category.getName()))
            throw new  IllegalArgumentException("Category already exists with name: " + category.getName());

        return  categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(UUID id) {
        Optional<Category>  category = categoryRepository.findById(id);
        if(category.isPresent()){
            if(!category.get().getPosts().isEmpty()) {
                throw new IllegalStateException("Category has posts associated with it");
            }
            categoryRepository.delete(category.get());
        }
    }

    @Override
    public Category getCategoryById(UUID categoryId) {
        return  categoryRepository.findById(categoryId)
                .orElseThrow(()->new EntityNotFoundException("Category with id " + categoryId + " not found"));

    }
}
