package com.khoahd7621.youngblack.entities;

import com.khoahd7621.youngblack.entities.composite.RatingKey;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "rating_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Rating {

    @EmbeddedId
    private RatingKey ratingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "number_of_stars")
    private int stars;
    @Column(name = "title")
    private String title;
    @Column(name = "comment")
    private String comment;
    @Column(name = "created_date")
    private Date createdDate;
    @Column(name = "is_show")
    private boolean isShow;
}
