package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.request.category.CreateNewCategoryRequest;
import com.khoahd7621.youngblack.dtos.request.category.UpdateNameCategoryRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.entities.Category;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.CategoryMapper;
import com.khoahd7621.youngblack.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class CategoryAdminServiceImplTest {

    private CategoryAdminServiceImpl categoryAdminServiceImpl;
    private CategoryRepository categoryRepository;
    private CategoryMapper categoryMapper;
    private CategoryResponse categoryResponse;

    @BeforeEach
    void beforeEach() {
        categoryRepository = mock(CategoryRepository.class);
        categoryMapper = mock(CategoryMapper.class);
        categoryAdminServiceImpl = CategoryAdminServiceImpl.builder()
                .categoryRepository(categoryRepository)
                .categoryMapper(categoryMapper).build();
        categoryResponse = mock(CategoryResponse.class);
    }

    @Test
    void createNewCategory_ShouldThrowException_WhenInValidDataRequest() {
        CreateNewCategoryRequest requestData = CreateNewCategoryRequest.builder().name("name").build();
        Category category = mock(Category.class);

        when(categoryRepository.findByNameAndIsDeletedFalse(requestData.getName())).thenReturn(Optional.of(category));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            categoryAdminServiceImpl.createNewCategory(requestData);
        });

        assertThat(actual.getMessage(), is("This category already existed."));
    }

    @Test
    void createNewCategory_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        CreateNewCategoryRequest requestData = CreateNewCategoryRequest.builder().name("name").build();
        SuccessResponse<CategoryResponse> expected =
                new SuccessResponse<>(categoryResponse, "Create new category successfully.");
        Category category = mock(Category.class);

        when(categoryRepository.findByNameAndIsDeletedFalse(requestData.getName())).thenReturn(Optional.empty());
        when(categoryMapper.toCategory(requestData)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        SuccessResponse<CategoryResponse> actual = categoryAdminServiceImpl.createNewCategory(requestData);

        verify(categoryRepository).save(category);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void getAllCategory_ShouldReturnData() {
        Category category = mock(Category.class);
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

        SuccessResponse<ListCategoriesResponse> actual = categoryAdminServiceImpl.getAllCategory();

        assertThat(actual.getData().getCategories(), is(expected.getData().getCategories()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void updateNameCategory_ShouldThrowBadRequestException_WhenInvalidCategoryIdInRequestData() {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).build();

        when(categoryRepository.findById(requestData.getId())).thenReturn(Optional.empty());

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            categoryAdminServiceImpl.updateNameCategory(requestData);
        });

        assertThat(actual.getMessage(), is("Id of category is not exist."));
    }

    @Test
    void updateNameCategory_ShouldThrowBadRequestException_WhenNameOfCategoryInRequestDataTheSameWithCategoryNameInDatabase() {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).newName("name").build();
        Category category = mock(Category.class);

        when(categoryRepository.findById(requestData.getId())).thenReturn(Optional.of(category));
        when(Optional.of(category).get().getName()).thenReturn("name");

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            categoryAdminServiceImpl.updateNameCategory(requestData);
        });

        assertThat(actual.getMessage(), is("New category name is the same with old category name. Nothing update."));
    }

    @Test
    void updateNameCategory_ShouldThrowBadRequestException_WhenNameOfCategoryInRequestDataDuplicated() {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).newName("name").build();
        Category category = mock(Category.class);

        when(categoryRepository.findById(requestData.getId())).thenReturn(Optional.of(category));
        when(Optional.of(category).get().getName()).thenReturn("different-name");
        when(categoryRepository.findByNameAndIsDeletedFalse(requestData.getNewName())).thenReturn(Optional.of(category));
        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            categoryAdminServiceImpl.updateNameCategory(requestData);
        });

        assertThat(actual.getMessage(), is("This category name already exist."));
    }

    @Test
    void updateNameCategory_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).newName("name").build();
        Category category = mock(Category.class);
        SuccessResponse<CategoryResponse> expected = new SuccessResponse<>(categoryResponse, "Update name category success.");

        when(categoryRepository.findById(requestData.getId())).thenReturn(Optional.of(category));
        when(Optional.of(category).get().getName()).thenReturn("different-name");
        when(categoryRepository.findByNameAndIsDeletedFalse(requestData.getNewName())).thenReturn(Optional.empty());
        when(categoryMapper.toCategoryResponse(category)).thenReturn(categoryResponse);

        SuccessResponse<CategoryResponse> actual = categoryAdminServiceImpl.updateNameCategory(requestData);

        verify(categoryRepository).save(category);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void deleteCategory_ShouldThrowNotFoundException_WhenInValidDataRequest() {
        int categoryIdRequest = 1;

        when(categoryRepository.findById(categoryIdRequest)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            categoryAdminServiceImpl.deleteCategory(categoryIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't exist category with this id"));
    }

    @Test
    void deleteCategory_ShouldThrowBadRequestException_WhenValidDataRequestButItHasProduct() {
        int categoryIdRequest = 1;
        Category category = mock(Category.class);
        Product product = mock(Product.class);
        Set<Product> productSet = new HashSet<>();
        productSet.add(product);

        when(categoryRepository.findById(categoryIdRequest)).thenReturn(Optional.of(category));
        when(Optional.of(category).get().getProducts()).thenReturn(productSet);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            categoryAdminServiceImpl.deleteCategory(categoryIdRequest);
        });

        assertThat(actual.getMessage(), is("This category had products. Cannot delete!"));
    }

    @Test
    void deleteCategory_ShouldThrowData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        int categoryIdRequest = 1;
        Category category = mock(Category.class);
        Set<Product> productSet = new HashSet<>();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "Delete category successfully.");

        when(categoryRepository.findById(categoryIdRequest)).thenReturn(Optional.of(category));
        when(Optional.of(category).get().getProducts()).thenReturn(productSet);

        SuccessResponse<NoData> actual = categoryAdminServiceImpl.deleteCategory(categoryIdRequest);

        verify(categoryRepository).save(category);
        assertThat(actual.getData().getNoData(), is(expected.getData().getNoData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}