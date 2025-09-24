package com.khoahd7621.youngblack.dtos.response.size;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListSizesResponse {
    private List<SizeResponse> sizes;
}
