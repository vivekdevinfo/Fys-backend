package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.entities.Category;
import com.khoahd7621.youngblack.mappers.CategoryMapper;
import com.khoahd7621.youngblack.repositories.CategoryRepository;
import com.khoahd7621.youngblack.services.CategoryService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Builder
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public SuccessResponse<ListCategoriesResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAllByIsDeletedFalse();
        List<CategoryResponse> categoryResponseList = categories.stream().map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        ListCategoriesResponse listCategoriesResponse =
                ListCategoriesResponse.builder().categories(categoryResponseList).build();
        return new SuccessResponse<>(listCategoriesResponse, "Get list category success.");
    }

}
