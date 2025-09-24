package com.khoahd7621.youngblack.controllers;

import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.image.ImageResponse;
import com.khoahd7621.youngblack.dtos.response.product.ProductResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailResponse;
import com.khoahd7621.youngblack.dtos.response.productvariant.ProductVariantResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.dtos.response.variantsize.VariantSizeResponse;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.ProductDetailService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductDetailController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
class ProductDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductDetailService productDetailService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getProductDetailBySlug_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        String slug = "slug";
        ProductResponse product = ProductResponse.builder()
                .productId(1)
                .name("name")
                .description("description")
                .price(1)
                .discountPrice(0)
                .startDateDiscount(null)
                .endDateDiscount(null)
                .slug("slug")
                .primaryImageUrl("primaryImageUrl")
                .secondaryImageUrl("secondaryImageUrl")
                .isPromotion(false)
                .category(CategoryResponse.builder().id(1).name("name").build()).build();
        List<ImageResponse> images = new ArrayList<>();
        images.add(ImageResponse.builder().imageId(1).imageUrl("imageUrl").imageName("imageName").build());
        List<VariantSizeResponse> sizes = new ArrayList<>();
        sizes.add(VariantSizeResponse.builder()
                .variantSizeId(1)
                .sku("sku")
                .isInStock(true)
                .size(SizeResponse.builder().id(1).size("size").build())
                .build());
        ProductVariantResponse productVariantResponse = ProductVariantResponse.builder()
                .variantId(1)
                .color(ColorResponse.builder().id(1).name("color").build())
                .images(images)
                .sizes(sizes).build();
        List<ProductVariantResponse> colors = new ArrayList<>();
        colors.add(productVariantResponse);
        ProductDetailResponse responseData = ProductDetailResponse.builder()
                .product(product)
                .colors(colors).build();
        SuccessResponse<ProductDetailResponse> expected = new SuccessResponse<>(responseData, "message");

        when(productDetailService.getProductDetailBySlug(slug)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product-detail")
                        .param("slug", slug))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"product\":{\"productId\":1,\"name\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"discountPrice\":0,\"startDateDiscount\":null,\"endDateDiscount\":null,\"slug\":\"slug\"," +
                        "\"primaryImageName\":null,\"primaryImageUrl\":\"primaryImageUrl\",\"secondaryImageName\":null," +
                        "\"secondaryImageUrl\":\"secondaryImageUrl\",\"category\":{\"id\":1,\"name\":\"name\"}," +
                        "\"promotion\":false,\"visible\":false}," +
                        "\"colors\":[{\"variantId\":1,\"color\":{\"id\":1,\"name\":\"color\"}," +
                        "\"images\":[{\"imageId\":1,\"imageName\":\"imageName\",\"imageUrl\":\"imageUrl\"}]," +
                        "\"sizes\":[{\"variantSizeId\":1,\"sku\":\"sku\",\"size\":{\"id\":1,\"size\":\"size\"},\"inStock\":true}]}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getProductDetailBySlug_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        String slug = "slug";
        NotFoundException notFoundException = new NotFoundException("message");

        when(productDetailService.getProductDetailBySlug(slug)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product-detail")
                        .param("slug", slug))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}