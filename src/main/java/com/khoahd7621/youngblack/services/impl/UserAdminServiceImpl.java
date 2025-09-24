package com.khoahd7621.youngblack.services.impl;

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
import com.khoahd7621.youngblack.services.UserAdminService;
import com.khoahd7621.youngblack.utils.PageableUtil;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
public class UserAdminServiceImpl implements UserAdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private PageableUtil pageableUtil;

    @Override
    public SuccessResponse<ListUsersWithPaginateResponse> getListUsersByRoleAndStatusWithPaginate(
            ERoles role, EAccountStatus status, Integer limit, Integer offset) {
        Pageable pageable = pageableUtil.getPageable(offset, limit);
        Page<User> userPage = userRepository.findAllByRoleAndStatus(role, status, pageable);
        List<UserResponse> userResponseList = userPage.getContent().stream().map(userMapper::toUserResponse)
                .collect(Collectors.toList());
        ListUsersWithPaginateResponse listUsersWithPaginateResponse =
                ListUsersWithPaginateResponse.builder()
                        .totalRows(userPage.getTotalElements())
                        .totalPages(userPage.getTotalPages())
                        .listUsers(userResponseList).build();
        return new SuccessResponse<>(listUsersWithPaginateResponse, "Get list users success.");
    }

    @Override
    public SuccessResponse<NoData> blockUserByUserId(long userId) throws NotFoundException, BadRequestException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Don't exist user with this id.");
        }
        User user = userOptional.get();
        if (user.getStatus().equals(EAccountStatus.BLOCK)) {
            throw new BadRequestException("You cannot block user has been blocked.");
        }
        user.setStatus(EAccountStatus.BLOCK);
        userRepository.save(user);
        return new SuccessResponse<>(NoData.builder().build(), "Block user success!");
    }

    @Override
    public SuccessResponse<NoData> unBlockUserByUserId(long userId) throws NotFoundException, BadRequestException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new NotFoundException("Don't exist user with this id.");
        }
        User user = userOptional.get();
        if (user.getStatus().equals(EAccountStatus.ACTIVE)) {
            throw new BadRequestException("You cannot un-block user has been activated.");
        }
        user.setStatus(EAccountStatus.ACTIVE);
        userRepository.save(user);
        return new SuccessResponse<>(NoData.builder().build(), "Un block user success!");
    }

    @Override
    public SuccessResponse<NoData> createNewAdminUser(CreateNewAdminUserRequest createNewAdminUserRequest) throws BadRequestException {
        if (!createNewAdminUserRequest.getPassword().equals(createNewAdminUserRequest.getConfirmPassword())) {
            throw new BadRequestException("Confirm password not match new password.");
        }
        Optional<User> userOptional = userRepository.findByEmail(createNewAdminUserRequest.getEmail());
        if (userOptional.isPresent()) {
            throw new BadRequestException("This email already existed.");
        }
        userOptional = userRepository.findByPhone(createNewAdminUserRequest.getPhone());
        if (userOptional.isPresent()) {
            throw new BadRequestException("This phone number already existed.");
        }
        User user = userMapper.toUser(createNewAdminUserRequest);
        userRepository.save(user);
        return new SuccessResponse<>(NoData.builder().build(), "Create new admin user successfully.");
    }
}
