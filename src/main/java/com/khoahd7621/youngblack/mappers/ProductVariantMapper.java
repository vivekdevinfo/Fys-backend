package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.color.ColorOfCreateNewProduct;
import com.khoahd7621.youngblack.dtos.response.productvariant.ProductVariantResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.entities.ProductVariant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProductVariantMapper {

    @Autowired
    private ColorMapper colorMapper;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private VariantSizeMapper variantSizeMapper;

    public ProductVariant toProductVariant(ColorOfCreateNewProduct colorOfCreateNewProduct, Product product) {
        return ProductVariant.builder()
                .color(colorMapper.toColor(colorOfCreateNewProduct))
                .product(product).build();
    }

    public ProductVariantResponse toProductVariantResponse(ProductVariant productVariant) {
        return ProductVariantResponse.builder()
                .variantId(productVariant.getId())
                .color(colorMapper.toColorResponse(productVariant.getColor()))
                .images(imageMapper.toListImageResponse(productVariant.getImages()))
                .sizes(variantSizeMapper.toListVariantSizeResponse(productVariant.getVariantSizes())).build();
    }

    public List<ProductVariantResponse> toListProductVariantResponse(Set<ProductVariant> productVariants) {
        return productVariants.stream().map(this::toProductVariantResponse).collect(Collectors.toList());
    }
}
