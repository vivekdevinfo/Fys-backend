package com.khoahd7621.youngblack.dtos.response.rating;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StarResponse {
    private int star;
    private double quantity;
    private double percent;
}
