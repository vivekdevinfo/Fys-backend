package com.khoahd7621.youngblack.dtos.response.productvariant;

import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.image.ImageResponse;
import com.khoahd7621.youngblack.dtos.response.variantsize.VariantSizeResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ProductVariantResponse {
    private long variantId;
    private ColorResponse color;
    private List<ImageResponse> images;
    private List<VariantSizeResponse> sizes;
}
