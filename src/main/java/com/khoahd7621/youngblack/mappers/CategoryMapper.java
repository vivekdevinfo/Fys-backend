package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.category.CategoryRequest;
import com.khoahd7621.youngblack.dtos.request.category.CreateNewCategoryRequest;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.entities.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public Category toCategory(CreateNewCategoryRequest createNewCategoryRequest) {
        return Category.builder().name(createNewCategoryRequest.getName()).isDeleted(false).build();
    }

    public Category toCategory(CategoryRequest categoryRequest) {
        return Category.builder()
                .id(categoryRequest.getId())
                .name(categoryRequest.getName()).build();
    }

    public CategoryResponse toCategoryResponse(Category category) {
        return CategoryResponse.builder().id(category.getId()).name(category.getName()).build();
    }
}
