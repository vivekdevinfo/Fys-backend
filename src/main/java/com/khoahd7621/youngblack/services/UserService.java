package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.dtos.request.user.UserChangePasswordRequest;
import com.khoahd7621.youngblack.dtos.response.user.UserResponse;
import com.khoahd7621.youngblack.dtos.request.user.UserUpdateRequest;

public interface UserService {

    public SuccessResponse<UserResponse> getCurrentUserInformation(long userId) throws NotFoundException, ForbiddenException;

    public SuccessResponse<UserResponse> updateCurrentUserInformation(long userId, UserUpdateRequest userUpdateRequest) throws BadRequestException, NotFoundException, ForbiddenException;

    public SuccessResponse<NoData> changePasswordOfCurrentUser(long userId, UserChangePasswordRequest userChangePasswordRequest) throws BadRequestException, NotFoundException, ForbiddenException;
}
