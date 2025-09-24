package com.khoahd7621.youngblack.entities;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "image_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "img_id")
    private long id;

    @Column(name = "img_name")
    private String name;

    @Column(name = "img_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;
}
