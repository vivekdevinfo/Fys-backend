package com.khoahd7621.youngblack.entities.composite;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDetailKey implements Serializable {
    @Column(name = "variant_size_id")
    private long variantSizeId;
    @Column(name = "order_id")
    private long orderId;
}
