package com.khoahd7621.youngblack.dtos.response.product;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListProductAdminWithPaginateResponse {
    private List<ProductAdminResponse> products;
    private long totalRows;
    private int totalPages;
}
