package com.dev.blog.service.impl;

import com.dev.blog.domain.entities.Category;
import com.dev.blog.repository.CategoryRepository;
import com.dev.blog.service.CategoryService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
