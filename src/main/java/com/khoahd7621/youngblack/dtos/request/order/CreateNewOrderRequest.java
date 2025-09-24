package com.khoahd7621.youngblack.dtos.request.order;

import com.khoahd7621.youngblack.constants.EDeliveryMethod;
import com.khoahd7621.youngblack.constants.EOrderStatus;
import com.khoahd7621.youngblack.constants.EPaymentMethod;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateNewOrderRequest {
    List<VariantSizeRequest> products;
    private String code;
    private String fullName;
    private String phone;
    private String address;
    private String note;
    private long totalPrice;
    private long deliveryFee;
    private EDeliveryMethod deliveryMethod;
    private EPaymentMethod paymentMethod;
}
