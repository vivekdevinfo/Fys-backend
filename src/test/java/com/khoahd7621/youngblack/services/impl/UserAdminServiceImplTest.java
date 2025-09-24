package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.dtos.request.user.CreateNewAdminUserRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.ListUsersWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.UserMapper;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.utils.PageableUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class UserAdminServiceImplTest {

    private UserAdminServiceImpl userAdminServiceImpl;
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PageableUtil pageableUtil;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void beforeEach() {
        userRepository = mock(UserRepository.class);
        userMapper = mock(UserMapper.class);
        pageableUtil = mock(PageableUtil.class);
        userAdminServiceImpl = UserAdminServiceImpl.builder()
                .userRepository(userRepository)
                .userMapper(userMapper)
                .pageableUtil(pageableUtil).build();
        user = mock(User.class);
        userResponse = mock(UserResponse.class);
    }

    @Test
    void getListUsersByRoleAndStatusWithPaginate_ShouldReturnData() {
        ERoles role = ERoles.USER;
        EAccountStatus status = EAccountStatus.ACTIVE;
        int limit = 5;
        int offset = 0;

        Pageable pageable = PageRequest.of(offset, limit);
        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        ArgumentCaptor<ERoles> roleCaptor = ArgumentCaptor.forClass(ERoles.class);
        ArgumentCaptor<EAccountStatus> statusCaptor = ArgumentCaptor.forClass(EAccountStatus.class);
        List<User> userList = new ArrayList<>();
        userList.add(user);
        Page<User> userPage = new PageImpl<>(userList);
        List<UserResponse> expectedUserResponseList = new ArrayList<>();
        expectedUserResponseList.add(userResponse);

        when(pageableUtil.getPageable(offset, limit)).thenReturn(pageable);
        when(userRepository.findAllByRoleAndStatus(roleCaptor.capture(), statusCaptor.capture(), pageableCaptor.capture()))
                .thenReturn(userPage);
        when(userMapper.toUserResponse(user)).thenReturn(userResponse);

        SuccessResponse<ListUsersWithPaginateResponse> actual =
                userAdminServiceImpl.getListUsersByRoleAndStatusWithPaginate(role, status, limit, offset);

        Pageable actualPageable = pageableCaptor.getValue();
        assertThat(actualPageable, is(pageable));

        ERoles actualRole = roleCaptor.getValue();
        assertThat(actualRole, is(role));

        EAccountStatus actualStatus = statusCaptor.getValue();
        assertThat(actualStatus, is(status));

        assertThat(actual.getCode(), is(0));
        assertThat(actual.getData().getListUsers(), is(expectedUserResponseList));
        assertThat(actual.getData().getTotalPages(), is(1));
        assertThat(actual.getData().getTotalRows(), is(1L));
        assertThat(actual.getMessage(), is("Get list users success."));
    }

    @Test
    void blockUserByUserId_ShouldThrowNotFoundException_WhenInvalidUserIdInDataRequest() {
        long userIdRequest = 1L;

        when(userRepository.findById(userIdRequest)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            userAdminServiceImpl.blockUserByUserId(userIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't exist user with this id."));
    }

    @Test
    void blockUserByUserId_ShouldThrowBadRequestException_WhenUserFoundByUserIdRequestAlreadyBlocked() {
        long userIdRequest = 1L;
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(userIdRequest)).thenReturn(userOptional);
        when(userOptional.get().getStatus()).thenReturn(EAccountStatus.BLOCK);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userAdminServiceImpl.blockUserByUserId(userIdRequest);
        });

        assertThat(actual.getMessage(), is("You cannot block user has been blocked."));
    }

    @Test
    void blockUserByUserId_ShouldReturnData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        long userIdRequest = 1L;
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(userIdRequest)).thenReturn(userOptional);
        when(userOptional.get().getStatus()).thenReturn(EAccountStatus.ACTIVE);

        SuccessResponse<NoData> actual = userAdminServiceImpl.blockUserByUserId(userIdRequest);

        verify(user).setStatus(EAccountStatus.BLOCK);
        verify(userRepository).save(user);
        assertThat(actual.getCode(), is(0));
        assertThat(actual.getData().getNoData(), is(NoData.builder().build().getNoData()));
        assertThat(actual.getMessage(), is("Block user success!"));
    }

    @Test
    void unBlockUserByUserId_ShouldThrowNotFoundException_WhenInvalidUserIdInDataRequest() {
        long userIdRequest = 1L;

        when(userRepository.findById(userIdRequest)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            userAdminServiceImpl.unBlockUserByUserId(userIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't exist user with this id."));
    }

    @Test
    void unBlockUserByUserId_ShouldThrowBadRequestException_WhenUserFoundByUserIdRequestAlreadyUnBlocked() {
        long userIdRequest = 1L;
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(userIdRequest)).thenReturn(userOptional);
        when(userOptional.get().getStatus()).thenReturn(EAccountStatus.ACTIVE);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userAdminServiceImpl.unBlockUserByUserId(userIdRequest);
        });

        assertThat(actual.getMessage(), is("You cannot un-block user has been activated."));
    }

    @Test
    void unBlockUserByUserId_ShouldReturnData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        long userIdRequest = 1L;
        Optional<User> userOptional = Optional.of(user);

        when(userRepository.findById(userIdRequest)).thenReturn(userOptional);
        when(userOptional.get().getStatus()).thenReturn(EAccountStatus.BLOCK);

        SuccessResponse<NoData> actual = userAdminServiceImpl.unBlockUserByUserId(userIdRequest);

        verify(user).setStatus(EAccountStatus.ACTIVE);
        verify(userRepository).save(user);
        assertThat(actual.getCode(), is(0));
        assertThat(actual.getData().getNoData(), is(NoData.builder().build().getNoData()));
        assertThat(actual.getMessage(), is("Un block user success!"));
    }

    @Test
    void createNewAdminUser_ShouldThrowBadRequestException_WhenConfirmPasswordNotMatchPasswordInDataRequest() {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .password("123456").confirmPassword("12345678").build();

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userAdminServiceImpl.createNewAdminUser(requestData);
        });

        assertThat(actual.getMessage(), is("Confirm password not match new password."));
    }

    @Test
    void createNewAdminUser_ShouldThrowBadRequestException_WhenEmailDuplicatedInDataRequest() {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .email("email@gmail.com").password("123456").confirmPassword("123456").build();

        when(userRepository.findByEmail(requestData.getEmail())).thenReturn(Optional.of(user));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userAdminServiceImpl.createNewAdminUser(requestData);
        });

        assertThat(actual.getMessage(), is("This email already existed."));
    }

    @Test
    void createNewAdminUser_ShouldThrowBadRequestException_WhenPhoneDuplicatedInDataRequest() {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .email("email@gmail.com").phone("0792596123")
                .password("123456").confirmPassword("123456").build();

        when(userRepository.findByEmail(requestData.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhone(requestData.getPhone())).thenReturn(Optional.of(user));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            userAdminServiceImpl.createNewAdminUser(requestData);
        });

        assertThat(actual.getMessage(), is("This phone number already existed."));
    }

    @Test
    void createNewAdminUser_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        CreateNewAdminUserRequest requestData = CreateNewAdminUserRequest.builder()
                .firstName("firstName").lastName("lastName")
                .email("email@gmail.com").phone("0792596123")
                .password("123456").confirmPassword("123456").build();

        when(userRepository.findByEmail(requestData.getEmail())).thenReturn(Optional.empty());
        when(userRepository.findByPhone(requestData.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toUser(requestData)).thenReturn(user);

        SuccessResponse<NoData> actual = userAdminServiceImpl.createNewAdminUser(requestData);

        verify(userRepository).save(user);
        assertThat(actual.getCode(), is(0));
        assertThat(actual.getData().getNoData(), is(NoData.builder().build().getNoData()));
        assertThat(actual.getMessage(), is("Create new admin user successfully."));
    }
}