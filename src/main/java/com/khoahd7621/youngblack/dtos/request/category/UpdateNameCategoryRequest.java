package com.khoahd7621.youngblack.dtos.request.category;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UpdateNameCategoryRequest {
    @Min(value = 0, message = "Id must be equal or greater than 0")
    private Integer id;

    @NotBlank(message = "New name of category need to update is required")
    private String newName;
}
