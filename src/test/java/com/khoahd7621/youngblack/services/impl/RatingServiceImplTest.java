package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.request.rating.CreateNewRatingRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.rating.RatingResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.entities.Rating;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.entities.composite.RatingKey;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.RatingMapper;
import com.khoahd7621.youngblack.repositories.ProductRepository;
import com.khoahd7621.youngblack.repositories.RatingRepository;
import com.khoahd7621.youngblack.services.AuthService;
import com.khoahd7621.youngblack.utils.PageableUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

class RatingServiceImplTest {

    private RatingServiceImpl ratingServiceImpl;
    private RatingRepository ratingRepository;
    private ProductRepository productRepository;
    private AuthService authService;
    private RatingMapper ratingMapper;
    private PageableUtil pageableUtil;

    private Rating rating;
    private Product product;
    private RatingResponse ratingResponse;

    @BeforeEach
    void beforeEach() {
        ratingRepository = mock(RatingRepository.class);
        productRepository = mock(ProductRepository.class);
        authService = mock(AuthService.class);
        ratingMapper = mock(RatingMapper.class);
        pageableUtil = mock(PageableUtil.class);
        ratingServiceImpl = RatingServiceImpl.builder()
                .ratingRepository(ratingRepository)
                .productRepository(productRepository)
                .authService(authService)
                .ratingMapper(ratingMapper)
                .pageableUtil(pageableUtil).build();
        rating = mock(Rating.class);
        product = mock(Product.class);
        ratingResponse = mock(RatingResponse.class);
    }

    @Test
    void createNewRatingProductOfUser_ShouldThrowNotFoundException_WhenNotExistProductIdOfDataRequest() {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder().productId(1).build();

        when(productRepository.findByIsDeletedFalseAndId(requestData.getProductId())).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            ratingServiceImpl.createNewRatingProductOfUser(requestData);
        });

        assertThat(actual.getMessage(), is("Don't exist product with this id."));
    }

    @Test
    void createNewRatingProductOfUser_ShouldThrowBadRequestException_WhenUserAlreadyRated() throws NotFoundException {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder().productId(1).build();
        Optional<Product> productOptional = Optional.of(product);
        User user = mock(User.class);
        Optional<Rating> ratingOptional = Optional.of(rating);

        when(productRepository.findByIsDeletedFalseAndId(requestData.getProductId())).thenReturn(productOptional);
        when(authService.getUserLoggedIn()).thenReturn(user);
        when(ratingRepository.findByRatingId(RatingKey.builder().userId(user.getId())
                .productId(productOptional.get().getId()).build())).thenReturn(ratingOptional);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            ratingServiceImpl.createNewRatingProductOfUser(requestData);
        });

        assertThat(actual.getMessage(), is("You already rated this product."));
    }

    @Test
    void createNewRatingProductOfUser_ShouldReturnData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder()
                .productId(1).star(5).title("title").content("content").build();
        Optional<Product> productOptional = Optional.of(product);
        User user = mock(User.class);
        SuccessResponse<NoData> expected =
                new SuccessResponse<>(NoData.builder().build(), "Create new rating successfully.");

        when(productRepository.findByIsDeletedFalseAndId(requestData.getProductId())).thenReturn(productOptional);
        when(authService.getUserLoggedIn()).thenReturn(user);
        when(ratingRepository.findByRatingId(RatingKey.builder().userId(user.getId())
                .productId(productOptional.get().getId()).build())).thenReturn(Optional.empty());
        when(ratingMapper.toRating(requestData, user, productOptional.get())).thenReturn(rating);

        SuccessResponse<NoData> actual = ratingServiceImpl.createNewRatingProductOfUser(requestData);

        verify(ratingRepository).save(rating);
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getData().getNoData(), is(expected.getData().getNoData()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void getAllRatingsOfProductWithPaginateAndSort_ShouldThrowNotFoundException_WhenNotExistProductIdInDataRequest() {
        int productId = 1;
        int offset = 0;
        int limit = 5;
        String sortType = "DESC";

        when(productRepository.findByIsDeletedFalseAndId(productId)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            ratingServiceImpl.getAllRatingsOfProductWithPaginateAndSort(productId, offset, limit, sortType);
        });

        assertThat(actual.getMessage(), is("Don't exist product with this id."));
    }

    @Test
    void getAllRatingsOfProductWithPaginateAndSort_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        // Todo
    }

    @Test
    void calculateStarPercent() {
        // Todo
    }

    @Test
    void calculateAverageRate() {
        // Todo
    }
}