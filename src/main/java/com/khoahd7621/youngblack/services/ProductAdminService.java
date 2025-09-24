package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.request.product.CreateNewProductRequest;
import com.khoahd7621.youngblack.dtos.request.product.UpdateProductRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductAdminWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailAdminResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;

public interface ProductAdminService {
    public SuccessResponse<NoData> createNewProduct(CreateNewProductRequest createNewProductRequest) throws BadRequestException;

    public SuccessResponse<ListProductAdminWithPaginateResponse> getAllProductWithPaginate(int limit, int offset);

    public SuccessResponse<ProductDetailAdminResponse> getProductDetailByProductId(Integer productId) throws NotFoundException;

    public SuccessResponse<NoData> deleteProductByProductId(Integer productId) throws NotFoundException;

    public SuccessResponse<NoData> updateProductByProductId(Integer productId, UpdateProductRequest updateProductRequest)
            throws NotFoundException, BadRequestException;
}
