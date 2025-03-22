package com.dev.blog.service;

import com.dev.blog.domain.entities.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category createCategory(Category category);
}
