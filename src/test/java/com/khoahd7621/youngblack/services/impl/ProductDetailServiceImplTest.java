package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.ProductDetailMapper;
import com.khoahd7621.youngblack.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class ProductDetailServiceImplTest {

    private ProductDetailServiceImpl productDetailServiceImpl;
    private ProductRepository productRepository;
    private ProductDetailMapper productDetailMapper;

    private Product product;
    private ProductDetailResponse productDetailResponse;

    @BeforeEach
    void beforeEach() {
        productRepository = mock(ProductRepository.class);
        productDetailMapper = mock(ProductDetailMapper.class);
        productDetailServiceImpl = ProductDetailServiceImpl.builder()
                .productRepository(productRepository)
                .productDetailMapper(productDetailMapper).build();
        product = mock(Product.class);
        productDetailResponse = mock(ProductDetailResponse.class);
    }

    @Test
    void getProductDetailBySlug_ShouldThrowNotFoundException_WhenInValidSlugInDataRequest() {
        String slug = "slug";

        when(productRepository.findByIsDeletedFalseAndSlug(slug)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            productDetailServiceImpl.getProductDetailBySlug(slug);
        });

        assertThat(actual.getMessage(), is("Don't exist product with this slug."));
    }

    @Test
    void getProductDetailBySlug_ShouldReturnData_WhenValidSlugInDataRequest() throws NotFoundException {
        String slug = "slug";
        SuccessResponse<ProductDetailResponse> expected =
                new SuccessResponse<>(productDetailResponse, "Get product detail success.");

        when(productRepository.findByIsDeletedFalseAndSlug(slug)).thenReturn(Optional.of(product));
        when(productDetailMapper.toProductDetailResponse(product)).thenReturn(productDetailResponse);

        SuccessResponse<ProductDetailResponse> actual = productDetailServiceImpl.getProductDetailBySlug(slug);

        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}