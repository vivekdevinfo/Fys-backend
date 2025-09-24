package com.khoahd7621.youngblack.dtos.response.color;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListColorsResponse {
    List<ColorResponse> colors;
}
