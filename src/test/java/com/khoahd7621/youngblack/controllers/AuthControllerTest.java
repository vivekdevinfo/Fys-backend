package com.khoahd7621.youngblack.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.khoahd7621.youngblack.App;
import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.dtos.request.user.UserLoginRequest;
import com.khoahd7621.youngblack.dtos.request.user.UserRegisterRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CorsConfig;
import com.khoahd7621.youngblack.security.WebSecurityConfig;
import com.khoahd7621.youngblack.services.AuthService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@ContextConfiguration(classes = {App.class, WebSecurityConfig.class, CorsConfig.class})
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private AuthService authService;
    @MockBean
    private JwtTokenUtil jwtTokenUtil;
    @MockBean
    private UserRepository userRepository;

    @Test
    void login_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        UserLoginRequest requestData = UserLoginRequest.builder()
                .email("email@gmail.com")
                .password("password").build();
        UserLoginResponse responseData = UserLoginResponse.builder()
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
        SuccessResponse<UserLoginResponse> expected =
                new SuccessResponse<>(responseData, "Login successfully.");

        when(authService.loginHandler(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{" +
                        "\"id\":1," +
                        "\"email\":\"email\"," +
                        "\"firstName\":\"firstName\"," +
                        "\"lastName\":\"lastName\"," +
                        "\"phone\":\"phone\"," +
                        "\"address\":\"address\"," +
                        "\"role\":\"USER\"," +
                        "\"status\":\"ACTIVE\"," +
                        "\"createdAt\":null," +
                        "\"updatedAt\":null," +
                        "\"accessToken\":null," +
                        "\"refreshToken\":null}," +
                        "\"message\":\"Login successfully.\"}"));
    }

    @Test
    void login_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        UserLoginRequest requestData = UserLoginRequest.builder()
                .email("email@gmail.com")
                .password("password").build();
        BadRequestException expected = new BadRequestException("Error message");

        when(authService.loginHandler(requestData)).thenThrow(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/login")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"Error message\"}"));
    }

    @Test
    void userRegister_ShouldReturnSuccessResponse_WhenValidDataRequest() throws Exception {
        UserRegisterRequest requestData = UserRegisterRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@gmail.com")
                .phone("0792596123")
                .password("password").build();
        SuccessResponse<NoData> expected = new SuccessResponse<>(
                NoData.builder().build(),
                "Register new account successfully.");

        when(authService.userRegister(requestData)).thenReturn(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/register")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is(
                "{\"code\":0," +
                        "\"data\":{\"noData\":\"No Data\"}," +
                        "\"message\":\"Register new account successfully.\"}"));
    }

    @Test
    void userRegister_ShouldThrowException_WhenInValidDataRequest() throws Exception {
        UserRegisterRequest requestData = UserRegisterRequest.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("email@gmail.com")
                .phone("0792596123")
                .password("password").build();
        BadRequestException expected = new BadRequestException("Error message");

        when(authService.userRegister(requestData)).thenThrow(expected);

        MockHttpServletResponse actual = mockMvc.perform(post("/api/v1/register")
                        .content(objectMapper.writeValueAsString(requestData))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse();

        assertThat(actual.getContentAsString(), is("{\"code\":-1,\"message\":\"Error message\"}"));
    }
}