package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.request.user.UserChangePasswordRequest;
import com.khoahd7621.youngblack.dtos.request.user.UserUpdateRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.UserMapper;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.services.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

class UserServiceImplTest {

    private UserServiceImpl userServiceImpl;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private AuthService authService;
    private PasswordEncoder passwordEncoder;

    private User user;
    private UserResponse userResponse;
    private NoData nodata;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        authService = mock(AuthService.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userServiceImpl = UserServiceImpl.builder()
                .userRepository(userRepository)
                .userMapper(userMapper)
                .authService(authService)
                .passwordEncoder(passwordEncoder).build();
        user = mock(User.class);
        userResponse = mock(UserResponse.class);
        nodata = mock(NoData.class);
    }

    @Test
    void getCurrentUserInformation_ShouldThrowForbiddenException_WhenInValidUserIdInDataRequest()
            throws NotFoundException {
        long userIdRequest = 1L;

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(2L);

        ForbiddenException actual = assertThrows(ForbiddenException.class, () -> {
            userServiceImpl.getCurrentUserInformation(userIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't have permission to do this action."));
    }

    @Test
    void getCurrentUserInformation_ShouldReturnData_WhenValidUserIdInDataRequest()
            throws NotFoundException, ForbiddenException {
        long userIdRequest = 1L;
        SuccessResponse<UserResponse> expected =
                new SuccessResponse<>(userResponse, "Get current user's information successfully.");

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        SuccessResponse<UserResponse> actual = userServiceImpl.getCurrentUserInformation(userIdRequest);

        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void updateCurrentUserInformation_ShouldThrowForbiddenException_WhenInValidUserIdInDataRequest()
            throws NotFoundException {
        long userIdRequest = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().build();

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(2L);

        ForbiddenException actual = assertThrows(ForbiddenException.class, () -> {
            userServiceImpl.updateCurrentUserInformation(userIdRequest, userUpdateRequest);
        });

        assertThat(actual.getMessage(), is("Don't have permission to do this action."));
    }

    @Test
    void updateCurrentUserInformation_ShouldThrowBadRequestException_WhenInValidPhoneInDataRequest()
            throws NotFoundException {
        long userIdRequest = 1L;
        Optional<User> optionalUser = Optional.of(user);
        user = mock(User.class);
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder().phone("0792596111").build();

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(userRepository.findByPhone(userUpdateRequest.getPhone())).thenReturn(optionalUser);
        when(optionalUser.get().getId()).thenReturn(2L);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.updateCurrentUserInformation(userIdRequest, userUpdateRequest);
        });

        assertThat(actual.getMessage(), is("This phone number already existed."));
    }

    @Test
    void updateCurrentUserInformation_ShouldReturnData_WhenValidDataRequest()
            throws NotFoundException, ForbiddenException, BadRequestException {
        long userIdRequest = 1L;
        UserUpdateRequest userUpdateRequest = UserUpdateRequest.builder()
                .firstName("firstName").lastName("lastName").phone("0792596111").address("address").build();
        SuccessResponse<UserResponse> expected =
                new SuccessResponse<>(userResponse, "Update information successfully.");

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(userRepository.findByPhone(userUpdateRequest.getPhone())).thenReturn(Optional.empty());
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        SuccessResponse<UserResponse> actual = userServiceImpl.updateCurrentUserInformation(userIdRequest, userUpdateRequest);

        verify(user).setFirstName("firstName");
        verify(user).setLastName("lastName");
        verify(user).setPhone("0792596111");
        verify(user).setAddress("address");
        verify(userRepository).save(user);
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowBadRequestException_WhenNewPasswordAndConfirmPasswordInDataRequestDifferent() {
        long userIdRequest = 1L;
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
                .newPassword("123456")
                .confirmPassword("123455").build();

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.changePasswordOfCurrentUser(userIdRequest, userChangePasswordRequest);
        });

        assertThat(actual.getMessage(), is("Confirm password not match new password."));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowForbiddenException_WhenInValidUserIdInDataRequest()
            throws NotFoundException {
        long userIdRequest = 1L;
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
                .newPassword("123456")
                .confirmPassword("123456").build();

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(2L);

        ForbiddenException actual = assertThrows(ForbiddenException.class, () -> {
            userServiceImpl.changePasswordOfCurrentUser(userIdRequest, userChangePasswordRequest);
        });

        assertThat(actual.getMessage(), is("Don't have permission to do this action."));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowBadRequestException_WhenOldPasswordInDataRequestNotMatch()
            throws NotFoundException {
        long userIdRequest = 1L;
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
                .oldPassword("1234567")
                .newPassword("123456")
                .confirmPassword("123456").build();

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())).thenReturn(false);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.changePasswordOfCurrentUser(userIdRequest, userChangePasswordRequest);
        });

        assertThat(actual.getMessage(), is("Old password is invalid."));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldThrowBadRequestException_WhenNewPasswordInDataSameWithPasswordInDatabase()
            throws NotFoundException {
        long userIdRequest = 1L;
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
                .oldPassword("1234567")
                .newPassword("123456")
                .confirmPassword("123456").build();

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword()))
                .thenReturn(true);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userServiceImpl.changePasswordOfCurrentUser(userIdRequest, userChangePasswordRequest);
        });

        assertThat(actual.getMessage(), is("New password is the same with old password. Nothing change."));
    }

    @Test
    void changePasswordOfCurrentUser_ShouldReturnData_WhenValidDataRequest()
            throws NotFoundException, ForbiddenException, BadRequestException {
        long userIdRequest = 1L;
        UserChangePasswordRequest userChangePasswordRequest = UserChangePasswordRequest.builder()
                .oldPassword("1234567")
                .newPassword("123456")
                .confirmPassword("123456").build();
        String passwordEncoded = "abcxyz";
        SuccessResponse<NoData> expected =
                new SuccessResponse<>(NoData.builder().build(), "Change password successfully.");

        when(authService.getUserLoggedIn()).thenReturn(user);
        when(user.getId()).thenReturn(1L);
        when(passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword()))
                .thenReturn(true);
        when(passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword()))
                .thenReturn(false);
        when(passwordEncoder.encode(userChangePasswordRequest.getNewPassword())).thenReturn(passwordEncoded);

        SuccessResponse<NoData> actual = userServiceImpl.changePasswordOfCurrentUser(userIdRequest, userChangePasswordRequest);

        verify(user).setPassword(passwordEncoded);
        verify(userRepository).save(user);
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getData().getNoData(), is(expected.getData().getNoData()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}