package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;

public interface CategoryService {
    public SuccessResponse<ListCategoriesResponse> getAllCategory();
}
