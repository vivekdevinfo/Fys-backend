package com.khoahd7621.youngblack.dtos.request.productvariant;

import com.khoahd7621.youngblack.dtos.request.color.ColorOfCreateNewProduct;
import com.khoahd7621.youngblack.dtos.request.image.ImageOfCreateNewProduct;
import com.khoahd7621.youngblack.dtos.request.size.SizeRequest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductVariantOfCreateNewProduct {
    private String sku;
    private ColorOfCreateNewProduct color;
    private List<ImageOfCreateNewProduct> images;
    private List<SizeRequest> sizes;
}
