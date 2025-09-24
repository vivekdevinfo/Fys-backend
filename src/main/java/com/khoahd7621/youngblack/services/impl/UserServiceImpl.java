package com.khoahd7621.youngblack.services.impl;

import java.util.Date;
import java.util.Optional;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.dtos.request.user.UserChangePasswordRequest;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.dtos.request.user.UserUpdateRequest;
import com.khoahd7621.youngblack.services.AuthService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.mappers.UserMapper;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.services.UserService;

@Service
@Builder
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public SuccessResponse<UserResponse> getCurrentUserInformation(long userId) throws NotFoundException, ForbiddenException {
        User user = authService.getUserLoggedIn();
        if (user.getId() != userId) {
            throw new ForbiddenException("Don't have permission to do this action.");
        }
        return new SuccessResponse<>(userMapper.toUserResponse(user),
                "Get current user's information successfully.");
    }

    @Override
    public SuccessResponse<UserResponse> updateCurrentUserInformation(long userId, UserUpdateRequest userUpdateRequest)
            throws BadRequestException, NotFoundException, ForbiddenException {
        User user = authService.getUserLoggedIn();
        if (user.getId() != userId) {
            throw new ForbiddenException("Don't have permission to do this action.");
        }
        Optional<User> userOptionalWithPhone = userRepository.findByPhone(userUpdateRequest.getPhone());
        if (userOptionalWithPhone.isPresent() && userOptionalWithPhone.get().getId() != user.getId()) {
            throw new BadRequestException("This phone number already existed.");
        }
        user.setFirstName(userUpdateRequest.getFirstName());
        user.setLastName(userUpdateRequest.getLastName());
        user.setPhone(userUpdateRequest.getPhone());
        user.setAddress(userUpdateRequest.getAddress());
        user.setUpdatedAt(new Date());
        userRepository.save(user);
        return new SuccessResponse<>(userMapper.toUserResponse(user), "Update information successfully.");
    }

    @Override
    public SuccessResponse<NoData> changePasswordOfCurrentUser(long userId, UserChangePasswordRequest userChangePasswordRequest)
            throws BadRequestException, NotFoundException, ForbiddenException {
        if (!userChangePasswordRequest.getNewPassword().equals(userChangePasswordRequest.getConfirmPassword())) {
            throw new BadRequestException("Confirm password not match new password.");
        }
        User user = authService.getUserLoggedIn();
        if (user.getId() != userId) {
            throw new ForbiddenException("Don't have permission to do this action.");
        }
        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is invalid.");
        }
        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new BadRequestException("New password is the same with old password. Nothing change.");
        }
        user.setPassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
        user.setUpdatedAt(new Date());
        userRepository.save(user);
        return new SuccessResponse<>(NoData.builder().build(), "Change password successfully.");
    }

}
