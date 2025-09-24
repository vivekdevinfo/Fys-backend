package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.dtos.request.rating.CreateNewRatingRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.rating.ListRatingsWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.rating.RatingResponse;
import com.khoahd7621.youngblack.dtos.response.rating.StarResponse;
import com.khoahd7621.youngblack.entities.Product;
import com.khoahd7621.youngblack.entities.Rating;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.entities.composite.RatingKey;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.RatingMapper;
import com.khoahd7621.youngblack.repositories.ProductRepository;
import com.khoahd7621.youngblack.repositories.RatingRepository;
import com.khoahd7621.youngblack.services.AuthService;
import com.khoahd7621.youngblack.services.RatingService;
import com.khoahd7621.youngblack.utils.PageableUtil;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Builder
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private AuthService authService;
    @Autowired
    private RatingMapper ratingMapper;
    @Autowired
    private PageableUtil pageableUtil;

    @Override
    public SuccessResponse<NoData> createNewRatingProductOfUser(CreateNewRatingRequest createNewRatingRequest)
            throws NotFoundException, BadRequestException {
        Optional<Product> productOptional = productRepository.findByIsDeletedFalseAndId(createNewRatingRequest.getProductId());
        if (productOptional.isEmpty()) {
            throw new NotFoundException("Don't exist product with this id.");
        }
        User user = authService.getUserLoggedIn();
        Optional<Rating> ratingOptional = ratingRepository.findByRatingId(RatingKey.builder().userId(user.getId())
                .productId(productOptional.get().getId()).build());
        if (ratingOptional.isPresent()) {
            throw new BadRequestException("You already rated this product.");
        }
        Rating rating = ratingMapper.toRating(createNewRatingRequest, user, productOptional.get());
        ratingRepository.save(rating);
        return new SuccessResponse<>(NoData.builder().build(), "Create new rating successfully.");
    }

    @Override
    public SuccessResponse<ListRatingsWithPaginateResponse> getAllRatingsOfProductWithPaginateAndSort(int productId, int offset, int limit, String sortType)
            throws BadRequestException, NotFoundException {
        Optional<Product> productOptional = productRepository.findByIsDeletedFalseAndId(productId);
        if (productOptional.isEmpty()) {
            throw new NotFoundException("Don't exist product with this id.");
        }
        Pageable pageable = pageableUtil.getPageable(offset, limit, "createdDate", sortType);
        Page<Rating> ratingPage = ratingRepository.findAllByIsShowTrueAndProductId(productId, pageable);
        List<RatingResponse> ratingResponseList = ratingPage.stream().map(ratingMapper::toRatingResponse)
                .collect(Collectors.toList());
        List<Rating> ratingList = ratingRepository.findAllByIsShowTrueAndProductId(productId);
        Map<String, StarResponse> resultOfCalculateStarPercent = calculateStarPercent(ratingList);
        double ratingAverage = calculateAverageRate(resultOfCalculateStarPercent);
        ListRatingsWithPaginateResponse listRatingsWithPaginateResponse =
                ListRatingsWithPaginateResponse.builder()
                        .ratings(ratingResponseList)
                        .stars(resultOfCalculateStarPercent)
                        .average(ratingAverage)
                        .totalRows(ratingPage.getTotalElements())
                        .totalPages(ratingPage.getTotalPages()).build();
        return new SuccessResponse<>(listRatingsWithPaginateResponse, "Get list ratings successfully.");
    }

    public Map<String, StarResponse> calculateStarPercent(List<Rating> ratings) {
        Map<String, StarResponse> stars = new HashMap<>();
        Map<Integer, Double> quantityStarMap = new HashMap<>();
        quantityStarMap.put(1, 0.0);
        quantityStarMap.put(2, 0.0);
        quantityStarMap.put(3, 0.0);
        quantityStarMap.put(4, 0.0);
        quantityStarMap.put(5, 0.0);
        for (Rating rating : ratings) {
            quantityStarMap.put(rating.getStars(), quantityStarMap.get(rating.getStars()) + 1);
        }
        for (Map.Entry<Integer, Double> entry : quantityStarMap.entrySet()) {
            stars.put(entry.getKey().toString(), StarResponse.builder()
                    .star(entry.getKey())
                    .quantity(entry.getValue())
                    .percent(entry.getValue() == 0.0 ? 0.0 : entry.getValue() / ratings.size() * 100).build());
        }
        return stars;
    }

    public double calculateAverageRate(Map<String, StarResponse> starResponseMap) {
        double totalStar = 0.0;
        double totalElements = 0.0;
        for (Map.Entry<String, StarResponse> entry : starResponseMap.entrySet()) {
            totalStar += entry.getValue().getQuantity() * entry.getValue().getStar();
            totalElements += entry.getValue().getQuantity();
        }
        return totalElements > 0 ? totalStar / totalElements : 0.0;
    }
}
