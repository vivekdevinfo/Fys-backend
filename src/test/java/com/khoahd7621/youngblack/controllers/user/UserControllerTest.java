package com.khoahd7621.youngblack.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.dtos.request.user.UserChangePasswordRequest;
import com.khoahd7621.youngblack.dtos.request.user.UserUpdateRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.UserService;
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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserService userService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    private UserResponse userResponse;
    private BadRequestException badRequestException;
    private NotFoundException notFoundException;
    private ForbiddenException forbiddenException;

    @BeforeEach
    void beforeEach() {
        userResponse = UserResponse.builder()
                .id(1)
                .email("email")
                .firstName("firstName")
                .lastName("lastName")
                .phone("phone")
                .address("address")
                .role(ERoles.USER)
                .status(EAccountStatus.ACTIVE)
                .createdAt(null)
                .updatedAt(null).build();
        badRequestException = new BadRequestException("message");
        notFoundException = new NotFoundException("message");
        forbiddenException = new ForbiddenException("message");
    }

    @Test
    void getCurrentUserInformation_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        long userId = 1L;
        SuccessResponse<UserResponse> expected = new SuccessResponse<>(userResponse, "message");

        when(userService.getCurrentUserInformation(userId)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/user/{userId}", userId))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"email\":\"email\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"," +
                        "\"phone\":\"phone\",\"address\":\"address\",\"role\":\"USER\",\"status\":\"ACTIVE\"," +
                        "\"createdAt\":null,\"updatedAt\":null}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void getCurrentUserInformation_ShouldThrowNotFoundException_WhenUnAuthentication() throws Exception {
        long userId = 1L;

        when(userService.getCurrentUserInformation(userId)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/user/{userId}", userId))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void getCurrentUserInformation_ShouldThrowForbiddenException_WhenWrongUserId() throws Exception {
        long userId = 1L;

        when(userService.getCurrentUserInformation(userId)).thenThrow(forbiddenException);

        MockHttpServletResponse actual = mockMvc.perform(get("/api/v1/user/{userId}", userId))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void updateCurrentUserInformation_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        long userId = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .firstName("firstName").lastName("lastName").phone("0792596123").address("address").build();
        SuccessResponse<UserResponse> expected = new SuccessResponse<>(userResponse, "message");

        when(userService.updateCurrentUserInformation(userId, userUpdateRequest)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"id\":1,\"email\":\"email\",\"firstName\":\"firstName\",\"lastName\":\"lastName\"," +
                        "\"phone\":\"phone\",\"address\":\"address\",\"role\":\"USER\",\"status\":\"ACTIVE\"," +
                        "\"createdAt\":null,\"updatedAt\":null}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void updateCurrentUserInformation_ShouldThrowBadRequestException_WhenDuplicatedPhoneInDataRequest() throws Exception {
        long userId = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .firstName("firstName").lastName("lastName").phone("0792596123").address("address").build();

        when(userService.updateCurrentUserInformation(userId, userUpdateRequest)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void updateCurrentUserInformation_ShouldThrowNotFoundException_WhenUnAuthentication() throws Exception {
        long userId = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .firstName("firstName").lastName("lastName").phone("0792596123").address("address").build();

        when(userService.updateCurrentUserInformation(userId, userUpdateRequest)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void updateCurrentUserInformation_ShouldThrowNotFoundException_WhenWrongUserId() throws Exception {
        long userId = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .firstName("firstName").lastName("lastName").phone("0792596123").address("address").build();

        when(userService.updateCurrentUserInformation(userId, userUpdateRequest)).thenThrow(forbiddenException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}", userId)
                        .content(objectMapper.writeValueAsString(userUpdateRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        long userId = 1L;
        UserChangePasswordRequest requestData = UserChangePasswordRequest.builder()
                .oldPassword("old-password").newPassword("new-password").confirmPassword("new-password").build();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "message");

        when(userService.changePasswordOfCurrentUser(userId, requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}/change-password", userId)
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"message\"}"));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowBadRequestException_WhenInValidDataRequest() throws Exception {
        long userId = 1L;
        UserChangePasswordRequest requestData = UserChangePasswordRequest.builder()
                .oldPassword("old-password").newPassword("new-password").confirmPassword("new-password").build();

        when(userService.changePasswordOfCurrentUser(userId, requestData)).thenThrow(badRequestException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}/change-password", userId)
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowNotFoundException_WhenUnAuthentication() throws Exception {
        long userId = 1L;
        UserChangePasswordRequest requestData = UserChangePasswordRequest.builder()
                .oldPassword("old-password").newPassword("new-password").confirmPassword("new-password").build();

        when(userService.changePasswordOfCurrentUser(userId, requestData)).thenThrow(notFoundException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}/change-password", userId)
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isNotFound())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowForbiddenException_WhenWrongUserId() throws Exception {
        long userId = 1L;
        UserChangePasswordRequest requestData = UserChangePasswordRequest.builder()
                .oldPassword("old-password").newPassword("new-password").confirmPassword("new-password").build();

        when(userService.changePasswordOfCurrentUser(userId, requestData)).thenThrow(forbiddenException);

        MockHttpServletResponse actual = mockMvc.perform(put("/api/v1/user/{userId}/change-password", userId)
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding("UTF-8"))
                .andExpect(status().isForbidden())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"message\"}"));
    }
}