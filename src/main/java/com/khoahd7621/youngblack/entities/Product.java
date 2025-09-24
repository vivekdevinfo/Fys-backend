package com.khoahd7621.youngblack.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "product_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<ProductVariant> productVariants;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Rating> ratings;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int id;

    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "slug")
    private String slug;
    @Column(name = "price")
    private long price;
    @Column(name = "discount_price")
    private long discountPrice;
    @Column(name = "start_date_discount")
    private Date startDateDiscount;
    @Column(name = "end_date_discount")
    private Date endDateDiscount;
    @Column(name = "primary_cover_img_name")
    private String primaryImageName;
    @Column(name = "primary_cover_img_url")
    private String primaryImageUrl;
    @Column(name = "secondary_cover_img_name")
    private String secondaryImageName;
    @Column(name = "secondary_cover_img_url")
    private String secondaryImageUrl;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @Column(name = "is_visible")
    private boolean isVisible;
    @Column(name = "is_deleted")
    private boolean isDeleted;
}
