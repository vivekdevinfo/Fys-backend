package com.khoahd7621.youngblack.dtos.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserRatingResponse {
    private long id;
    private String firstName;
    private String lastName;
}
