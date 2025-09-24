package com.khoahd7621.youngblack.entities;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "variant_size_tbl")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VariantSize {
    @OneToMany(mappedBy = "variantSize", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<OrderDetail> orderDetails;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_size_id")
    private long id;
    @Column(name = "sku")
    private String sku;
    @Column(name = "is_in_stock")
    private boolean isInStock;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_variant_id")
    private ProductVariant productVariant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "size_id")
    private Size size;
}
