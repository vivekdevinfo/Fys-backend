package com.khoahd7621.youngblack.dtos.request.user;

import com.khoahd7621.youngblack.constants.Constants;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class UserChangePasswordRequest {
    @NotBlank(message = "Old password is required")
    @Size(min = Constants.MIN_LENGTH_PASSWORD, max = Constants.MAX_LENGTH_PASSWORD, message = "The old password must contain at least 6 characters and be up to 24 characters")
    private String oldPassword;
    @NotBlank(message = "New password is required")
    @Size(min = Constants.MIN_LENGTH_PASSWORD, max = Constants.MAX_LENGTH_PASSWORD, message = "The new password must contain at least 6 characters and be up to 24 characters")
    private String newPassword;
    @NotBlank(message = "Confirm password is required")
    @Size(min = Constants.MIN_LENGTH_PASSWORD, max = Constants.MAX_LENGTH_PASSWORD, message = "The confirm password must contain at least 6 characters and be up to 24 characters")
    private String confirmPassword;
}
