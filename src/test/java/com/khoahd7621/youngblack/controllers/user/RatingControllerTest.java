package com.khoahd7621.youngblack.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.request.rating.CreateNewRatingRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.rating.ListRatingsWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.rating.RatingResponse;
import com.khoahd7621.youngblack.dtos.response.rating.StarResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserRatingResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.RatingService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RatingController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private RatingService ratingService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private BadRequestException badRequestException;
    private NotFoundException notFoundException;

    @BeforeEach
    void beforeEach() {
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
    }

    @Test
    void createNewRatingProductOfUser_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder()
                .productId(1).star(5).title("title").content("content").build();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(ratingService.createNewRatingProductOfUser(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/rating")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void createNewRatingProductOfUser_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder()
                .productId(1).star(5).title("title").content("content").build();

        when(ratingService.createNewRatingProductOfUser(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/rating")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void createNewRatingProductOfUser_ShouldThrowNotFoundException_WhenInValidDataRequest() throws Exception {
        CreateNewRatingRequest requestData = CreateNewRatingRequest.builder()
                .productId(1).star(5).title("title").content("content").build();

        when(ratingService.createNewRatingProductOfUser(requestData)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/rating")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllRatingsOfProductWithPaginateAndSort_ShouldReturnSuccessResponse_WhenValidDataRequest()
            throws Exception {
        int productId = 1;
        int offset = 0;
        int limit = 1;
        String sortType = "DESC";
        List<RatingResponse> ratings = new ArrayList<>();
        ratings.add(RatingResponse.builder()
                .user(UserRatingResponse.builder().id(1).firstName("firstName").lastName("lastName").build())
                .stars(5)
                .title("title")
                .comment("comment")
                .isShow(true)
                .createdDate(null).build());
        Map<String, StarResponse> stars = new HashMap<>();
        stars.put("1", StarResponse.builder().star(1).percent(1).quantity(1).build());
        ListRatingsWithPaginateResponse responseData = ListRatingsWithPaginateResponse.builder()
                .ratings(ratings)
                .stars(stars)
                .average(5)
                .totalRows(1)
                .totalPages(1).build();
        SuccessResponse<ListRatingsWithPaginateResponse> expected = new SuccessResponse<>(responseData, "message");

        when(ratingService.getAllRatingsOfProductWithPaginateAndSort(productId, offset, limit, sortType)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/rating")
                        .param("product-id", String.valueOf(productId))
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit))
                        .param("sort-type", sortType))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"ratings\":[{\"user\":{\"id\":1,\"firstName\":\"firstName\"," +
                        "\"lastName\":\"lastName\"},\"stars\":5,\"title\":\"title\",\"comment\":\"comment\"," +
                        "\"createdDate\":null,\"show\":true}],\"stars\":{\"1\":{\"star\":1,\"quantity\":1.0," +
                        "\"percent\":1.0}},\"average\":5.0,\"totalRows\":1,\"totalPages\":1}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getAllRatingsOfProductWithPaginateAndSort_ShouldThrowBadRequestException_WhenInValidOffsetOrLimitOrSortTypeInDataRequest()
            throws Exception {
        int productId = 1;
        int offset = 0;
        int limit = 1;
        String sortType = "DESC";

        when(ratingService.getAllRatingsOfProductWithPaginateAndSort(productId, offset, limit, sortType))
                .thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/rating")
                        .param("product-id", String.valueOf(productId))
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit))
                        .param("sort-type", sortType))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllRatingsOfProductWithPaginateAndSort_ShouldThrowNotFoundException_WhenInValidProductIdInDataRequest()
            throws Exception {
        int productId = 1;
        int offset = 0;
        int limit = 1;
        String sortType = "DESC";

        when(ratingService.getAllRatingsOfProductWithPaginateAndSort(productId, offset, limit, sortType))
                .thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/rating")
                        .param("product-id", String.valueOf(productId))
                        .param("offset", String.valueOf(offset))
                        .param("limit", String.valueOf(limit))
                        .param("sort-type", sortType))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}