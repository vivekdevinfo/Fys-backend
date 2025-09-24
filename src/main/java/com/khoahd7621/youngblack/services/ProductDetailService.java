package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailResponse;
import com.khoahd7621.youngblack.exceptions.NotFoundException;

public interface ProductDetailService {
    public SuccessResponse<ProductDetailResponse> getProductDetailBySlug(String slug) throws NotFoundException;
}
