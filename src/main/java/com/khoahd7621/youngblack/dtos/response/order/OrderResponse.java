package com.khoahd7621.youngblack.dtos.response.order;

import com.khoahd7621.youngblack.constants.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class OrderResponse {
    private long id;
    private String code;
    private String fullName;
    private String phone;
    private String address;
    private String note;
    private long totalPrice;
    private long deliveryFee;
    private EOrderStatus status;
    private Date orderDate;
    private Date finishDate;
    private EDeliveryMethod deliveryMethod;
    private EPaymentMethod paymentMethod;
    private EDeliveryStatus deliveryStatus;
    private EPaymentStatus paymentStatus;
}
