package com.khoahd7621.youngblack.controllers.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.dtos.request.user.CreateNewAdminUserRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.ListUsersWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.UserAdminService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserAdminController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class UserAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserAdminService userAdminService;
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
    void getListUsersByRoleAndStatusWithPaginate_ShouldReturnSuccessResponse() throws Exception {
        ERoles role = ERoles.USER;
        EAccountStatus status = EAccountStatus.ACTIVE;
        int limit = 1;
        int offset = 0;
        List<UserResponse> listUsers = new ArrayList<>();
        listUsers.add(UserResponse.builder()
                .id(1)
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .address("address")
                .role(ERoles.USER)
                .status(EAccountStatus.ACTIVE)
                .createdAt(null)
                .updatedAt(null).build());
        ListUsersWithPaginateResponse responseData = ListUsersWithPaginateResponse.builder()
                .listUsers(listUsers).totalPages(1).totalRows(1).build();
        SuccessResponse<ListUsersWithPaginateResponse> expected = new SuccessResponse<>(responseData, "message");

        when(userAdminService.getListUsersByRoleAndStatusWithPaginate(role, status, limit, offset)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/admin/user")
                        .param("role", role.toString())
                        .param("status", status.toString())
                        .param("limit", String.valueOf(limit))
                        .param("offset", String.valueOf(offset)))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"totalRows\":1," +
                        "\"totalPages\":1," +
                        "\"listUsers\":[{\"id\":1,\"email\":\"email\",\"firstName\":\"firstName\"," +
                        "\"lastName\":\"lastName\",\"phone\":\"phone\",\"address\":\"address\",\"role\":\"USER\"," +
                        "\"status\":\"ACTIVE\",\"createdAt\":null,\"updatedAt\":null}]}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void blockUserByUserId_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        long userId = 1L;
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(userAdminService.blockUserByUserId(userId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/block", userId))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void blockUserByUserId_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        long userId = 1L;

        when(userAdminService.blockUserByUserId(userId)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/block", userId))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void blockUserByUserId_ShouldThrowNotFoundException_WhenInValidDataRequest() throws Exception {
        long userId = 1L;

        when(userAdminService.blockUserByUserId(userId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/block", userId))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void unBlockUserByUserId_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        long userId = 1L;
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(userAdminService.unBlockUserByUserId(userId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/un-block", userId))
                .andExpect(status().isOk()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void unBlockUserByUserId_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        long userId = 1L;

        when(userAdminService.unBlockUserByUserId(userId)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/un-block", userId))
                .andExpect(status().isBadRequest()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void unBlockUserByUserId_ShouldThrowNotFoundException_WhenInValidDataRequest() throws Exception {
        long userId = 1L;

        when(userAdminService.unBlockUserByUserId(userId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/admin/user/{userId}/un-block", userId))
                .andExpect(status().isNotFound()).andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void createNewAdminUser_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@gmail.com")
                .phone("0792123456")
                .password("password")
                .confirmPassword("confirmPassword").build();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(userAdminService.createNewAdminUser(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/user")
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
    void createNewAdminUser_ShouldThrowException_WhenValidDataRequest() throws Exception {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@gmail.com")
                .phone("0792123456")
                .password("password")
                .confirmPassword("confirmPassword").build();

        when(userAdminService.createNewAdminUser(requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/admin/user")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}