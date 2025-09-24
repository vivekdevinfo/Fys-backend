package com.khoahd7621.youngblack.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.request.color.CreateNewColorRequest;
import com.khoahd7621.youngblack.dtos.request.color.UpdateColorNameRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.color.ListColorsResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.ColorAdminService;
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

@WebMvcTest(ColorAdminController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class ColorAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ColorAdminService colorAdminService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private ColorResponse colorResponse;
    private BadRequestException badRequestException;
    private NotFoundException notFoundException;

    @BeforeEach
    void beforeEach() {
        colorResponse = ColorResponse.builder().id(1).name("name").build();
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
    }

    @Test
    void createNewColor_ShouldReturnSuccessData_WhenValidDataRequest() throws Exception {
        CreateNewColorRequest requestData = CreateNewColorRequest.builder().name("name").build();
        SuccessResponse<ColorResponse> expected = new SuccessResponse<>(colorResponse, "message");

        when(colorAdminService.createNewColor(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/color")
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
    void createNewColor_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        CreateNewColorRequest requestData = CreateNewColorRequest.builder().name("name").build();

        when(colorAdminService.createNewColor(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/color")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllColors_ShouldReturnSuccessData() throws Exception {
        List<ColorResponse> colorResponseList = new ArrayList<>();
        colorResponseList.add(colorResponse);
        ListColorsResponse listColorsResponse = ListColorsResponse.builder().colors(colorResponseList).build();
        SuccessResponse<ListColorsResponse> expected = new SuccessResponse<>(listColorsResponse, "message");

        when(colorAdminService.getAllColors()).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/admin/color"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"colors\":[{\"id\":1,\"name\":\"name\"}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateColorName_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).newName("name").build();
        SuccessResponse<ColorResponse> expected = new SuccessResponse<>(colorResponse, "message");

        when(colorAdminService.updateColorName(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/color")
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
    void updateColorName_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).newName("name").build();

        when(colorAdminService.updateColorName(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/color")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteColor_ShouldReturnSuccessData_WhenValidDataRequest() throws Exception {
        int colorId = 1;
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(colorAdminService.deleteColor(colorId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/color/{colorId}", colorId))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void deleteColor_ShouldThrowNotFoundRequestException_WhenInValidDataRequest() throws Exception {
        int colorId = 1;

        when(colorAdminService.deleteColor(colorId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/color/{colorId}", colorId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteColor_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        int colorId = 1;

        when(colorAdminService.deleteColor(colorId)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/color/{colorId}", colorId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}