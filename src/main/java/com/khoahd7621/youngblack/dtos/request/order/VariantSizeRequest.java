package com.khoahd7621.youngblack.dtos.request.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VariantSizeRequest {
    private Long variantSizeId;
    private Integer quantity;
    private Long price;
}
