package com.khoahd7621.youngblack.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.dtos.request.size.CreateNewSizeRequest;
import com.khoahd7621.youngblack.dtos.request.size.UpdateSizeRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.size.ListSizesResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.SizeAdminService;
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

@WebMvcTest(SizeAdminController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class SizeAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private SizeAdminService sizeAdminService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private SizeResponse sizeResponse;
    private BadRequestException badRequestException;
    private NotFoundException notFoundException;

    @BeforeEach
    void beforeEach() {
        sizeResponse = SizeResponse.builder().id(1).size("size").build();
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
    }

    @Test
    void createNewSize_ShouldReturnSuccessData_WhenValidDataRequest() throws Exception {
        CreateNewSizeRequest requestData = CreateNewSizeRequest.builder().size("size").build();
        SuccessResponse<SizeResponse> expected = new SuccessResponse<>(sizeResponse, "message");

        when(sizeAdminService.createNewSize(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/size")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"size\":\"size\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void createNewSize_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        CreateNewSizeRequest requestData = CreateNewSizeRequest.builder().size("size").build();

        when(sizeAdminService.createNewSize(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/size")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getAllSizes_ShouldReturnSuccessData() throws Exception {
        List<SizeResponse> sizeResponseList = new ArrayList<>();
        sizeResponseList.add(sizeResponse);
        ListSizesResponse listSizesResponse = ListSizesResponse.builder().sizes(sizeResponseList).build();
        SuccessResponse<ListSizesResponse> expected = new SuccessResponse<>(listSizesResponse, "message");

        when(sizeAdminService.getAllSizes()).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/admin/size"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"sizes\":[{\"id\":1,\"size\":\"size\"}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateSize_ShouldReturnSuccessData_WhenValidDataRequest() throws Exception {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).newSize("size").build();
        SuccessResponse<SizeResponse> expected = new SuccessResponse<>(sizeResponse, "message");

        when(sizeAdminService.updateSize(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/size")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"size\":\"size\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateSize_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).newSize("size").build();

        when(sizeAdminService.updateSize(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/size")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteSize_ShouldReturnSuccessData_WhenValidDataRequest() throws Exception {
        int sizeId = 1;
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(sizeAdminService.deleteSize(sizeId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/size/{sizeId}", sizeId))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void deleteSize_ShouldThrowNotFoundException_WhenInValidDataRequest() throws Exception {
        int sizeId = 1;

        when(sizeAdminService.deleteSize(sizeId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/size/{sizeId}", sizeId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void deleteSize_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        int sizeId = 1;

        when(sizeAdminService.deleteSize(sizeId)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(delete("/api/v1/admin/size/{sizeId}", sizeId))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}