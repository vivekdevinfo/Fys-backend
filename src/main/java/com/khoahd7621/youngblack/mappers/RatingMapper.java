package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.rating.CreateNewRatingRequest;
import com.khoahd7621.youngblack.dtos.response.rating.RatingResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.entities.Rating;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.entities.composite.RatingKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class RatingMapper {

    @Autowired
    private UserMapper userMapper;

    public Rating toRating(CreateNewRatingRequest createNewRatingRequest, User user, Product product) {
        return Rating.builder()
                .ratingId(RatingKey.builder().userId(user.getId()).productId(product.getId()).build())
                .user(user)
                .product(product)
                .stars(createNewRatingRequest.getStar())
                .title(createNewRatingRequest.getTitle())
                .comment(createNewRatingRequest.getContent())
                .isShow(true)
                .createdDate(new Date()).build();
    }

    public RatingResponse toRatingResponse(Rating rating) {
        return RatingResponse.builder()
                .user(userMapper.toUserRatingResponse(rating.getUser()))
                .stars(rating.getStars())
                .title(rating.getTitle())
                .comment(rating.getComment())
                .createdDate(rating.getCreatedDate())
                .isShow(rating.isShow()).build();
    }
}
