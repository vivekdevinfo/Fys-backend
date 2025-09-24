package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.dtos.request.user.CreateNewAdminUserRequest;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserRatingResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.dtos.request.user.UserRegisterRequest;

import lombok.RequiredArgsConstructor;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User toUser(UserRegisterRequest userRegisterRequest) {
        return User.builder()
                .email(userRegisterRequest.getEmail())
                .phone(userRegisterRequest.getPhone())
                .firstName(userRegisterRequest.getFirstName())
                .lastName(userRegisterRequest.getLastName())
                .password(passwordEncoder.encode(userRegisterRequest.getPassword()))
                .role(ERoles.USER)
                .status(EAccountStatus.ACTIVE)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public User toUser(CreateNewAdminUserRequest createNewAdminUserRequest) {
        return User.builder()
                .email(createNewAdminUserRequest.getEmail())
                .phone(createNewAdminUserRequest.getPhone())
                .firstName(createNewAdminUserRequest.getFirstName())
                .lastName(createNewAdminUserRequest.getLastName())
                .password(passwordEncoder.encode(createNewAdminUserRequest.getPassword()))
                .role(ERoles.ADMIN)
                .status(EAccountStatus.ACTIVE)
                .createdAt(new Date())
                .updatedAt(new Date())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    public UserLoginResponse toUserLoginResponse(User user) {
        return UserLoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .phone(user.getPhone())
                .address(user.getAddress())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt()).build();
    }

    public UserRatingResponse toUserRatingResponse(User user) {
        return UserRatingResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName()).build();
    }
}
