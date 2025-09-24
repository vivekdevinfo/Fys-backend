package com.khoahd7621.youngblack.dtos.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListProductResponse {
    private List<ProductResponse> products;
}
