package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailAdminResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailResponse;
import com.khoahd7621.youngblack.entities.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDetailMapper {

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private ProductVariantMapper productVariantMapper;

    public ProductDetailResponse toProductDetailResponse(Product product) {
        return ProductDetailResponse.builder()
                .product(productMapper.toProductResponse(product))
                .colors(productVariantMapper.toListProductVariantResponse(product.getProductVariants())).build();
    }

    public ProductDetailAdminResponse toProductDetailAdminResponse(Product product) {
        return ProductDetailAdminResponse.builder()
                .product(productMapper.toProductAdminResponse(product))
                .colors(productVariantMapper.toListProductVariantResponse(product.getProductVariants())).build();
    }
}
