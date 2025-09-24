package com.khoahd7621.youngblack.entities;

import com.khoahd7621.youngblack.entities.composite.OrderDetailKey;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "order_detail_tbl")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {

    @EmbeddedId
    private OrderDetailKey orderDetailId;

    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private long price;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("variantSizeId")
    @JoinColumn(name = "variant_size_id")
    private VariantSize variantSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
}
