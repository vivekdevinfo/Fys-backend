package com.khoahd7621.youngblack.dtos.request.rating;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class CreateNewRatingRequest {
    @Min(0)
    private Integer productId;
    @Min(value = 0, message = "Number of star must be equal or greater than 0")
    @Max(value = 5, message = "Number of star must be equal or lower than 5")
    private Integer star;
    @NotBlank(message = "Title is required")
    private String title;
    @NotBlank(message = "Content is required")
    private String content;
}
