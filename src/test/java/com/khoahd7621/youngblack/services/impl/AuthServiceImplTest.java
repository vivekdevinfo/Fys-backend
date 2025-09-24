package com.khoahd7621.youngblack.services.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.dtos.request.user.UserRegisterRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.dtos.request.user.UserLoginRequest;
import com.khoahd7621.youngblack.mappers.UserMapper;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class AuthServiceImplTest {

    private AuthServiceImpl authServiceImpl;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private UserMapper userMapper;
    private JwtTokenUtil jwtTokenUtil;
    private SecurityContext securityContext;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userMapper = mock(UserMapper.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        securityContext = mock(SecurityContext.class);
        authServiceImpl = new AuthServiceImpl(userRepository, passwordEncoder, userMapper, jwtTokenUtil, securityContext);
    }

    @Test
    void loginHandler_ShouldReturnData_WhenRequestDataValidAndAccountStatusIsActive() throws BadRequestException {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("email").password("password").build();
        User user = mock(User.class);
        UserLoginResponse userLoginResponse = mock(UserLoginResponse.class);
        SuccessResponse<UserLoginResponse> expected = new SuccessResponse<>(userLoginResponse, "Login successfully.");

        when(userRepository.findByEmail(userLoginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(user.getStatus()).thenReturn(EAccountStatus.ACTIVE);
        when(userMapper.toUserLoginResponse(user)).thenReturn(userLoginResponse);
        when(jwtTokenUtil.generateAccessToken(user)).thenReturn("Token");
        when(jwtTokenUtil.generateRefreshToken(user)).thenReturn("Refresh_Token");

        SuccessResponse<UserLoginResponse> actual = authServiceImpl.loginHandler(userLoginRequest);

        assertThat(actual.getData(), is(userLoginResponse));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void loginHandler_ShouldReturnData_WhenRequestDataValidAndAccountStatusIsInActive() throws BadRequestException {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("email").password("password").build();
        User user = mock(User.class);

        when(userRepository.findByEmail(userLoginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(user.getStatus()).thenReturn(EAccountStatus.INACTIVE);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            authServiceImpl.loginHandler(userLoginRequest);
        });
        assertThat(actual.getMessage(), is("The account has not been activated."));
    }

    @Test
    void loginHandler_ShouldReturnData_WhenRequestDataValidAndAccountStatusIsBlock() throws BadRequestException {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("email").password("password").build();
        User user = mock(User.class);

        when(userRepository.findByEmail(userLoginRequest.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())).thenReturn(true);
        when(user.getStatus()).thenReturn(EAccountStatus.BLOCK);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            authServiceImpl.loginHandler(userLoginRequest);
        });

        assertThat(actual.getMessage(), is("Account has been blocked."));
    }

    @Test
    void loginHandler_ShouldReturnData_WhenRequestDataInValid() throws BadRequestException {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .email("email").password("password").build();

        when(userRepository.findByEmail(userLoginRequest.getEmail())).thenReturn(Optional.empty());

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            authServiceImpl.loginHandler(userLoginRequest);
        });
        assertThat(actual.getMessage(), is("Email or password is incorrect."));
    }

    @Test
    void userRegister_ShouldReturnData_WhenDataValid() throws BadRequestException {
        User user = mock(User.class);
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email")
                .phone("0123123")
                .build();
        NoData noData = NoData.builder().build();
        SuccessResponse<NoData> expected = new SuccessResponse<>(noData, "Register successfully.");
        when(userRepository.findByEmail(userRegisterRequest.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhone(userRegisterRequest.getPhone())).thenReturn(Optional.empty());
        when(userMapper.toUser(userRegisterRequest)).thenReturn(user);

        SuccessResponse<NoData> actual = authServiceImpl.userRegister(userRegisterRequest);

        verify(userRepository).save(user);
        assertThat(actual.getData().getNoData(), is(noData.getNoData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void userRegister_ShouldReturnError_WhenDuplicatedEmail() throws BadRequestException {
        User user = mock(User.class);
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .email("email@gmail.com")
                .build();

        when(userRepository.findByEmail(userRegisterRequest.getEmail())).thenReturn(Optional.of(user));

        BadRequestException result = assertThrows(BadRequestException.class, () -> {
            authServiceImpl.userRegister(userRegisterRequest);
        });

        assertThat(result.getMessage(), is("This email already exists."));
    }

    @Test
    void userRegister_ShouldReturnError_WhenDuplicatedPhone() throws BadRequestException {
        User user = mock(User.class);
        UserRegisterRequest userRegisterRequest = UserRegisterRequest.builder()
                .phone("0123123")
                .build();

        when(userRepository.findByPhone(userRegisterRequest.getPhone())).thenReturn(Optional.of(user));

        BadRequestException result = assertThrows(BadRequestException.class, () -> {
            authServiceImpl.userRegister(userRegisterRequest);
        });
        assertThat(result.getMessage(), is("This phone number already exists."));
    }
}