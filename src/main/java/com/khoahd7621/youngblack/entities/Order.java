package com.khoahd7621.youngblack.entities;

import com.khoahd7621.youngblack.constants.*;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "order_tbl", indexes = @Index(name = "order_code_index", columnList = "order_code", unique = true))
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @OneToMany(mappedBy = "order", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    Set<OrderDetail> orderDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private long id;
    @Column(name = "order_code")
    private String code;
    @Column(name = "full_name")
    private String fullName;
    @Column(name = "phone_number")
    private String phone;
    @Column(name = "address")
    private String address;
    @Column(name = "note")
    private String note;
    @Column(name = "total_price")
    private long totalPrice;
    @Column(name = "delivery_fee")
    private long deliveryFee;
    @Column(name = "order_status")
    private EOrderStatus status;
    @Column(name = "order_date")
    private Date orderDate;
    @Column(name = "confirm_date")
    private Date confirmDate;
    @Column(name = "finish_date")
    private Date finishDate;
    @Column(name = "delivery_method")
    private EDeliveryMethod deliveryMethod;
    @Column(name = "payment_method")
    private EPaymentMethod paymentMethod;
    @Column(name = "delivery_status")
    private EDeliveryStatus deliveryStatus;
    @Column(name = "payment_status")
    private EPaymentStatus paymentStatus;
}
