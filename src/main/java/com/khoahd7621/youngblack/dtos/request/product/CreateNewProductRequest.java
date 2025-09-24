package com.khoahd7621.youngblack.dtos.request.product;

import com.khoahd7621.youngblack.dtos.request.category.CategoryRequest;
import com.khoahd7621.youngblack.dtos.request.productvariant.ProductVariantOfCreateNewProduct;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateNewProductRequest {
    private String name;
    private Long price;
    private String description;
    private Boolean isVisible;
    private String primaryImageName;
    private String primaryImageUrl;
    private String secondaryImageName;
    private String secondaryImageUrl;

    private CategoryRequest category;
    private List<ProductVariantOfCreateNewProduct> colors;
}
