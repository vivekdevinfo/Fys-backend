package com.khoahd7621.youngblack.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.category.CategoryResponse;
import com.khoahd7621.youngblack.dtos.response.category.ListCategoriesResponse;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.CategoryService;
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

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.MatcherAssert.assertThat;

@WebMvcTest(CategoryController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private CategoryService categoryService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    @Test
    void getAllCategory_ShouldReturnListCategories() throws Exception {
        List<CategoryResponse> categories = new ArrayList<>();
        categories.add(CategoryResponse.builder().id(1).name("category").build());
        ListCategoriesResponse responseData = ListCategoriesResponse.builder().categories(categories).build();
        SuccessResponse<ListCategoriesResponse> expected = new SuccessResponse<>(responseData, "message");

        when(categoryService.getAllCategory()).thenReturn(expected);

        MockHttpServletResponse response = mockMvc.perform(get("/api/v1/category"))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(response.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"categories\":[{\"id\":1,\"name\":\"category\"}]}," +
                        "\"message\":\"message\"}"));
    }
}