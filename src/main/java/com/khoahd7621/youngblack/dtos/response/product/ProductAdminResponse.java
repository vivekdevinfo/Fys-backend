package com.khoahd7621.youngblack.dtos.response.product;

import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class ProductAdminResponse {
    private int productId;
    private String name;
    private String description;
    private long price;
    private long discountPrice;
    private Date startDateDiscount;
    private Date endDateDiscount;
    private String slug;
    private String primaryImageName;
    private String primaryImageUrl;
    private String secondaryImageName;
    private String secondaryImageUrl;
    private boolean isVisible;
    private boolean isPromotion;
    private CategoryResponse category;
}
