package com.khoahd7621.youngblack.dtos.response.productdetail;

import com.khoahd7621.youngblack.dtos.response.product.ProductAdminResponse;
import com.khoahd7621.youngblack.dtos.response.productvariant.ProductVariantResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductDetailAdminResponse {
    private ProductAdminResponse product;
    private List<ProductVariantResponse> colors;
}
