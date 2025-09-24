package com.khoahd7621.youngblack.dtos.response.order;

import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class OrderDetailResponse {
    private String name;
    private String slug;
    private String imageUrl;
    private ColorResponse color;
    private SizeResponse size;
    private CategoryResponse category;
    private int quantity;
    private long price;
}
