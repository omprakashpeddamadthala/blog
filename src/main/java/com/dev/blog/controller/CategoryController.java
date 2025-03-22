package com.dev.blog.controller;

import com.dev.blog.domain.dtos.CategoryDTO;
import com.dev.blog.domain.dtos.CategoryRequest;
import com.dev.blog.domain.entities.Category;
import com.dev.blog.mappers.CategoryMapper;
import com.dev.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/api/v1/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        log.info("Get all categories");
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(
                categories.stream().map(categoryMapper::toDto).toList()
        );
    }


    @PostMapping
    public ResponseEntity<?> createCategory(@Valid  @RequestBody CategoryRequest categoryRequest) {
        log.info( "Create new category with name {}", categoryRequest.getName());
        Category category =categoryMapper.toEntity( categoryRequest );
        Category savedCategory = categoryService.createCategory( category );
        return new ResponseEntity<>(
                categoryMapper.toDto( savedCategory ),
                HttpStatus.CREATED );
    }
}
