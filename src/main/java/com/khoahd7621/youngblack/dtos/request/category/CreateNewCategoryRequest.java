package com.khoahd7621.youngblack.dtos.request.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CreateNewCategoryRequest {
    @NotBlank(message = "Name of category is required")
    private String name;
}
