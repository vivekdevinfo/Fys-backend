package com.khoahd7621.youngblack.controllers;

import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.product.ProductResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.ProductService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
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
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private ProductResponse productResponse;
    private List<ProductResponse> productResponseList;
    private ListProductWithPaginateResponse listProductWithPaginateResponse;
    private BadRequestException badRequestException;
    private NotFoundException notFoundException;

    @BeforeEach
    void beforeEach() throws Exception {
        productResponse = ProductResponse.builder()
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
        productResponseList = new ArrayList<>();
        productResponseList.add(productResponse);
        listProductWithPaginateResponse = ListProductWithPaginateResponse.builder()
                .products(productResponseList).totalRows(1).totalPages(1).build();
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
    }

    @Test
    void getAllProductsWithPaginateAndSort_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        int offset = 0;
        int limit = 1;
        String sortBase = "sort-base";
        String sortType = "sort-type";
        SuccessResponse<ListProductWithPaginateResponse> expected =
                new SuccessResponse<>(listProductWithPaginateResponse, "message");

        when(productService.getAllProductsWithPaginateAndSort(offset, limit, sortBase, sortType))
                .thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product/all")
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset))
                        .param("sort-base", sortBase)
                        .param("sort-type", sortType))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"products\":[{\"productId\":1,\"name\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"discountPrice\":0,\"startDateDiscount\":null,\"endDateDiscount\":null,\"slug\":\"slug\"," +
                        "\"primaryImageName\":null,\"primaryImageUrl\":\"primaryImageUrl\",\"secondaryImageName\":null," +
                        "\"secondaryImageUrl\":\"secondaryImageUrl\",\"category\":{\"id\":1,\"name\":\"name\"}," +
                        "\"promotion\":false,\"visible\":false}]," +
                        "\"totalRows\":1," +
                        "\"totalPages\":1}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getAllProductsWithPaginateAndSort_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        int offset = 0;
        int limit = 1;
        String sortBase = "sort-base";
        String sortType = "sort-type";

        when(productService.getAllProductsWithPaginateAndSort(offset, limit, sortBase, sortType))
                .thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product/all")
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset))
                        .param("sort-base", sortBase)
                        .param("sort-type", sortType))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllProductsByCategoryNameWithPaginateAndSort_ShouldReturnSuccessResponse_WhenValidDataRequest()
            throws Exception {
        String categoryName = "category-name";
        int offset = 0;
        int limit = 1;
        String sortBase = "sort-base";
        String sortType = "sort-type";
        SuccessResponse<ListProductWithPaginateResponse> expected =
                new SuccessResponse<>(listProductWithPaginateResponse, "message");

        when(productService.getAllProductsByCategoryNameWithPaginateAndSort(categoryName, offset, limit, sortBase, sortType))
                .thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product")
                        .param("category-name", categoryName)
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset))
                        .param("sort-base", sortBase)
                        .param("sort-type", sortType))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"products\":[{\"productId\":1,\"name\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"discountPrice\":0,\"startDateDiscount\":null,\"endDateDiscount\":null,\"slug\":\"slug\"," +
                        "\"primaryImageName\":null,\"primaryImageUrl\":\"primaryImageUrl\",\"secondaryImageName\":null," +
                        "\"secondaryImageUrl\":\"secondaryImageUrl\"," +
                        "\"category\":{\"id\":1,\"name\":\"name\"}," +
                        "\"promotion\":false,\"visible\":false}]," +
                        "\"totalRows\":1," +
                        "\"totalPages\":1}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getAllProductsByCategoryNameWithPaginateAndSort_ShouldThrowException_WhenInValidDataRequest()
            throws Exception {
        String categoryName = "category-name";
        int offset = 0;
        int limit = 1;
        String sortBase = "sort-base";
        String sortType = "sort-type";

        when(productService.getAllProductsByCategoryNameWithPaginateAndSort(categoryName, offset, limit, sortBase, sortType))
                .thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product")
                        .param("category-name", categoryName)
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset))
                        .param("sort-base", sortBase)
                        .param("sort-type", sortType))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllProductsSearchByNameWithPaginate_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        String query = "query";
        int offset = 0;
        int limit = 1;
        SuccessResponse<ListProductWithPaginateResponse> expected =
                new SuccessResponse<>(listProductWithPaginateResponse, "message");

        when(productService.getAllProductsSearchByNameWithPaginate(query, offset, limit)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product/search")
                        .param("query", query)
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset)))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"products\":[{\"productId\":1,\"name\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"discountPrice\":0,\"startDateDiscount\":null,\"endDateDiscount\":null,\"slug\":\"slug\"," +
                        "\"primaryImageName\":null,\"primaryImageUrl\":\"primaryImageUrl\",\"secondaryImageName\":null," +
                        "\"secondaryImageUrl\":\"secondaryImageUrl\"," +
                        "\"category\":{\"id\":1,\"name\":\"name\"}," +
                        "\"promotion\":false,\"visible\":false}]," +
                        "\"totalRows\":1," +
                        "\"totalPages\":1}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getNRelatedProductByCategoryId_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        int categoryId = 1;
        int numberElements = 1;
        ListProductResponse listProductResponse = ListProductResponse.builder().products(productResponseList).build();
        SuccessResponse<ListProductResponse> expected = new SuccessResponse<>(listProductResponse, "message");

        when(productService.getNRelatedProductByCategoryId(categoryId, numberElements)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product/related")
                        .param("category-id", String.valueOf(categoryId))
                        .param("number-elements", String.valueOf(numberElements)))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"products\":[{\"productId\":1,\"name\":\"name\",\"description\":\"description\",\"price\":1," +
                        "\"discountPrice\":0,\"startDateDiscount\":null,\"endDateDiscount\":null,\"slug\":\"slug\"," +
                        "\"primaryImageName\":null,\"primaryImageUrl\":\"primaryImageUrl\",\"secondaryImageName\":null," +
                        "\"secondaryImageUrl\":\"secondaryImageUrl\"," +
                        "\"category\":{\"id\":1,\"name\":\"name\"}," +
                        "\"promotion\":false,\"visible\":false}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getNRelatedProductByCategoryId_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        int categoryId = 1;
        int numberElements = 1;

        when(productService.getNRelatedProductByCategoryId(categoryId, numberElements))
                .thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/product/related")
                        .param("category-id", String.valueOf(categoryId))
                        .param("number-elements", String.valueOf(numberElements)))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}