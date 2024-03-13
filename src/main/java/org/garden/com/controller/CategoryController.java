package org.garden.com.controller;

import org.garden.com.converter.CategoryMapper;
import org.garden.com.dto.CategoryCreateDto;
import org.garden.com.dto.CategoryDto;
import org.garden.com.entity.Category;
import org.garden.com.exceptions.InvalidCategoryException;
import org.garden.com.service.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    @Autowired
    CategoryServiceImpl categoryService;

    @Autowired
    CategoryMapper mapper;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        try {
            List<Category> categories = categoryService.getAllCategories();
            List<CategoryDto> categoryDtoList = categories.stream()
                    .map(category -> mapper.categoryToCategoryDto(category))
                    .collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(categoryDtoList);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<CategoryCreateDto> createProduct(@RequestBody CategoryCreateDto categoryCreateDto) {
        try {
            Category category = mapper.createCategoryDtoToCategory(categoryCreateDto);
            Category createdCategory = categoryService.createCategory(category);
            CategoryCreateDto createdCategoryDto = mapper.categoryToCreateCategoryDto(createdCategory);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCategoryDto);
        } catch (InvalidCategoryException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }


}
