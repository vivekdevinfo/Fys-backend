package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.request.user.UserRegisterRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.dtos.request.user.UserLoginRequest;

public interface AuthService {
    public SuccessResponse<UserLoginResponse> loginHandler(UserLoginRequest userLoginRequest) throws BadRequestException;

    public User getUserLoggedIn() throws NotFoundException;

    public SuccessResponse<NoData> userRegister(UserRegisterRequest userRegisterRequest) throws BadRequestException;
}
