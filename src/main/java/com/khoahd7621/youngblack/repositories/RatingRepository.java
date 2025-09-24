package com.khoahd7621.youngblack.repositories;

import com.khoahd7621.youngblack.entities.Rating;
import com.khoahd7621.youngblack.entities.composite.RatingKey;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, RatingKey> {
    public Optional<Rating> findByRatingId(RatingKey ratingId);

    public Page<Rating> findAllByIsShowTrueAndProductId(int productId, Pageable pageable);

    public List<Rating> findAllByIsShowTrueAndProductId(int productId);
}
