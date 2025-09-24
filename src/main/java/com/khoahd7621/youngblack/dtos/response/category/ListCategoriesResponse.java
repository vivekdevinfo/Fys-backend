package com.khoahd7621.youngblack.dtos.response.category;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListCategoriesResponse {
    List<CategoryResponse> categories;
}
