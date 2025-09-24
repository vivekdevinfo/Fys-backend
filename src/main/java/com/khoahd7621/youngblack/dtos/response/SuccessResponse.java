package com.khoahd7621.youngblack.dtos.response;

import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.color.ListColorsResponse;
import com.khoahd7621.youngblack.dtos.response.image.UploadImageResponse;
import com.khoahd7621.youngblack.dtos.response.order.CreateNewOrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.ListOrdersResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderWithDetailResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductAdminWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailAdminResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailResponse;
import com.khoahd7621.youngblack.dtos.response.rating.ListRatingsWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.size.ListSizesResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.dtos.response.user.ListUsersWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> {
    private final int code = 0;
    @Schema(anyOf = {
            UserResponse.class,
            NoData.class,
            ListUsersWithPaginateResponse.class,
            SizeResponse.class,
            ListSizesResponse.class,
            ListProductAdminWithPaginateResponse.class,
            ProductDetailAdminResponse.class,
            ColorResponse.class,
            ListColorsResponse.class,
            CategoryResponse.class,
            ListCategoriesResponse.class,
            UserLoginResponse.class,
            ListRatingsWithPaginateResponse.class,
            CreateNewOrderResponse.class,
            OrderWithDetailResponse.class,
            ListOrdersResponse.class,
            UploadImageResponse.class,
            ListProductWithPaginateResponse.class,
            ListProductResponse.class,
            ProductDetailResponse.class,
            ListCategoriesResponse.class
    })
    private T data;
    private String message;
}
