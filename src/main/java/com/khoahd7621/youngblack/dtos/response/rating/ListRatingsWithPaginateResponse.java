package com.khoahd7621.youngblack.dtos.response.rating;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Builder
public class ListRatingsWithPaginateResponse {
    private List<RatingResponse> ratings;
    private Map<String, StarResponse> stars;
    private double average;
    private long totalRows;
    private int totalPages;
}
