package com.khoahd7621.youngblack.services.impl;

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
import com.khoahd7621.youngblack.services.CategoryAdminService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Builder
public class CategoryAdminServiceImpl implements CategoryAdminService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public SuccessResponse<CategoryResponse> createNewCategory(CreateNewCategoryRequest createNewCategoryRequest)
            throws BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findByNameAndIsDeletedFalse(createNewCategoryRequest.getName());
        if (categoryOptional.isPresent()) {
            throw new BadRequestException("This category already existed.");
        }
        Category category = categoryMapper.toCategory(createNewCategoryRequest);
        Category result = categoryRepository.save(category);
        return new SuccessResponse<>(categoryMapper.toCategoryResponse(result), "Create new category successfully.");
    }

    @Override
    public SuccessResponse<ListCategoriesResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAllByIsDeletedFalse();
        List<CategoryResponse> categoryResponseList = categories.stream().map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
        ListCategoriesResponse listCategoriesResponse =
                ListCategoriesResponse.builder().categories(categoryResponseList).build();
        return new SuccessResponse<>(listCategoriesResponse, "Get list category success.");
    }

    @Override
    public SuccessResponse<CategoryResponse> updateNameCategory(UpdateNameCategoryRequest updateNameCategoryRequest) throws BadRequestException {
        Optional<Category> categoryOptionalFindById = categoryRepository.findById(updateNameCategoryRequest.getId());
        if (categoryOptionalFindById.isEmpty()) {
            throw new BadRequestException("Id of category is not exist.");
        }
        if (categoryOptionalFindById.get().getName().equals(updateNameCategoryRequest.getNewName())) {
            throw new BadRequestException("New category name is the same with old category name. Nothing update.");
        }
        Optional<Category> categoryOptionalFindByName = categoryRepository.findByNameAndIsDeletedFalse(updateNameCategoryRequest.getNewName());
        if (categoryOptionalFindByName.isPresent()) {
            throw new BadRequestException("This category name already exist.");
        }
        Category category = categoryOptionalFindById.get();
        category.setName(updateNameCategoryRequest.getNewName());
        categoryRepository.save(category);
        return new SuccessResponse<>(categoryMapper.toCategoryResponse(category), "Update name category success.");
    }

    @Override
    public SuccessResponse<NoData> deleteCategory(int categoryId) throws NotFoundException, BadRequestException {
        Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
        if (categoryOptional.isEmpty()) {
            throw new NotFoundException("Don't exist category with this id");
        }
        Set<Product> productSet = categoryOptional.get().getProducts();
        if (productSet.size() > 0) {
            throw new BadRequestException("This category had products. Cannot delete!");
        }
        Category category = categoryOptional.get();
        category.setDeleted(true);
        categoryRepository.save(category);
        return new SuccessResponse<>(NoData.builder().build(), "Delete category successfully.");
    }
}
