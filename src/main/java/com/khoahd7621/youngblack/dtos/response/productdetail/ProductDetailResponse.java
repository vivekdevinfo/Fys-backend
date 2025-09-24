package com.khoahd7621.youngblack.dtos.response.productdetail;

import com.khoahd7621.youngblack.dtos.response.product.ProductResponse;
import com.khoahd7621.youngblack.dtos.response.productvariant.ProductVariantResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDetailResponse {
    private ProductResponse product;
    private List<ProductVariantResponse> colors;
}
