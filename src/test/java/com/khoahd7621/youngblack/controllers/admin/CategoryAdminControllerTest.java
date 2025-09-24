package com.khoahd7621.youngblack.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.request.category.CreateNewCategoryRequest;
import com.khoahd7621.youngblack.dtos.request.category.UpdateNameCategoryRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.CategoryAdminService;
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
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CategoryAdminController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class CategoryAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private CategoryAdminService categoryAdminService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private CategoryResponse categoryResponse;
    private BadRequestException badRequestException;
    private NotFoundException notFoundException;

    @BeforeEach
    void beforeEach() {
        categoryResponse = CategoryResponse.builder().id(1).name("name").build();
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
    }

    @Test
    void createNewCategory_ShouldReturnData_WhenValidDataRequest() throws Exception {
        CreateNewCategoryRequest requestData = CreateNewCategoryRequest.builder()
                .name("category").build();
        SuccessResponse<CategoryResponse> expected = new SuccessResponse<>(categoryResponse, "message");

        when(categoryAdminService.createNewCategory(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/category")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"name\":\"name\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void createNewCategory_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        CreateNewCategoryRequest requestData = CreateNewCategoryRequest.builder()
                .name("category").build();

        when(categoryAdminService.createNewCategory(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/category")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllCategory_ShouldReturnData() throws Exception {
        List<CategoryResponse> categories = new ArrayList<>();
        categories.add(categoryResponse);
        ListCategoriesResponse responseData = ListCategoriesResponse.builder().categories(categories).build();
        SuccessResponse<ListCategoriesResponse> expected = new SuccessResponse<>(responseData, "message");

        when(categoryAdminService.getAllCategory()).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/admin/category"))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"categories\":[{\"id\":1,\"name\":\"name\"}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateNameCategory_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).newName("name").build();
        SuccessResponse<CategoryResponse> expected = new SuccessResponse<>(categoryResponse, "message");

        when(categoryAdminService.updateNameCategory(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/category")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"name\":\"name\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateNameCategory_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        UpdateNameCategoryRequest requestData = UpdateNameCategoryRequest.builder().id(1).newName("name").build();

        when(categoryAdminService.updateNameCategory(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/category")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteCategory_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        int categoryId = 1;
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(categoryAdminService.deleteCategory(categoryId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(
                        delete("/api/v1/admin/category/{categoryId}", categoryId))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void deleteCategory_ShouldThrowNotFoundException_WhenInValidDataRequest() throws Exception {
        int categoryId = 1;

        when(categoryAdminService.deleteCategory(categoryId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(
                        delete("/api/v1/admin/category/{categoryId}", categoryId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteCategory_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        int categoryId = 1;

        when(categoryAdminService.deleteCategory(categoryId)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(
                        delete("/api/v1/admin/category/{categoryId}", categoryId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}