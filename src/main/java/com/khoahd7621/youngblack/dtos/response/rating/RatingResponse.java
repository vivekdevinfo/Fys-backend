package com.khoahd7621.youngblack.dtos.response.rating;

import com.khoahd7621.youngblack.dtos.response.user.UserRatingResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class RatingResponse {
    private UserRatingResponse user;
    private int stars;
    private String title;
    private String comment;
    private Date createdDate;
    private boolean isShow;
}
