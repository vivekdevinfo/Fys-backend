package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.entities.Category;
import com.khoahd7621.youngblack.mappers.CategoryMapper;
import com.khoahd7621.youngblack.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class CategoryServiceImplTest {

    private CategoryServiceImpl categoryServiceImpl;
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private Category category;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void beforeEach() {
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        categoryServiceImpl = CategoryServiceImpl.builder()
                .categoryRepository(categoryRepository)
                .categoryMapper(categoryMapper).build();
        category = mock(Category.class);
        categoryResponse = mock(CategoryResponse.class);
    }

    @Test
    void getAllCategory_ShouldReturnData() {
        List<Category> categories = new ArrayList<>();
        categories.add(category);
        List<CategoryResponse> categoryResponseList = new ArrayList<>();
        categoryResponseList.add(categoryResponse);
        ListCategoriesResponse listCategoriesResponse =
                ListCategoriesResponse.builder().categories(categoryResponseList).build();
        SuccessResponse<ListCategoriesResponse> expected =
                new SuccessResponse<>(listCategoriesResponse, "Get list category success.");

        when(categoryRepository.findAllByIsDeletedFalse()).thenReturn(categories);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        SuccessResponse<ListCategoriesResponse> actual = categoryServiceImpl.getAllCategory();

        assertThat(actual.getData().getCategories(), is(expected.getData().getCategories()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}