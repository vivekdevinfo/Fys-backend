package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.dtos.request.user.UserRegisterRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.UserLoginResponse;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.dtos.request.user.UserLoginRequest;
import com.khoahd7621.youngblack.mappers.UserMapper;
import com.khoahd7621.youngblack.repositories.UserRepository;
import com.khoahd7621.youngblack.security.CustomUserDetails;
import com.khoahd7621.youngblack.services.AuthService;
import com.khoahd7621.youngblack.utils.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private SecurityContext securityContext;

    @Override
    public SuccessResponse<UserLoginResponse> loginHandler(UserLoginRequest userLoginRequest)
            throws BadRequestException {
        Optional<User> userOpt = userRepository.findByEmail(userLoginRequest.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(userLoginRequest.getPassword(), user.getPassword())) {
                if (user.getStatus() == EAccountStatus.ACTIVE) {
                    UserLoginResponse userLoginResponse = userMapper.toUserLoginResponse(user);
                    userLoginResponse.setAccessToken(jwtTokenUtil.generateAccessToken(user));
                    userLoginResponse.setRefreshToken(jwtTokenUtil.generateRefreshToken(user));
                    return new SuccessResponse<>(userLoginResponse, "Login successfully.");
                }
                if (user.getStatus() == EAccountStatus.INACTIVE) {
                    throw new BadRequestException("The account has not been activated.");
                }
                if (user.getStatus() == EAccountStatus.BLOCK) {
                    throw new BadRequestException("Account has been blocked.");
                }
            }
        }
        throw new BadRequestException("Email or password is incorrect.");
    }

    @Override
    public User getUserLoggedIn() throws NotFoundException {
        Object principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        throw new NotFoundException("User does not exist.");
    }

    @Override
    public SuccessResponse<NoData> userRegister(UserRegisterRequest userRegisterRequest) throws BadRequestException {
        Optional<User> userOpt = userRepository.findByEmail(userRegisterRequest.getEmail());
        if (userOpt.isPresent()) {
            throw new BadRequestException("This email already exists.");
        }
        userOpt = userRepository.findByPhone(userRegisterRequest.getPhone());
        if (userOpt.isPresent()) {
            throw new BadRequestException("This phone number already exists.");
        }
        User user = userMapper.toUser(userRegisterRequest);
        userRepository.save(user);
        return new SuccessResponse<>(NoData.builder().build(), "Register successfully.");
    }

}
