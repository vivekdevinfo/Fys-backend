package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.constants.EDeliveryStatus;
import com.khoahd7621.youngblack.constants.EOrderStatus;
import com.khoahd7621.youngblack.constants.EPaymentStatus;
import com.khoahd7621.youngblack.dtos.request.order.CreateNewOrderRequest;
import com.khoahd7621.youngblack.dtos.response.order.OrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderWithDetailResponse;
import com.khoahd7621.youngblack.entities.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class OrderMapper {

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    public Order toOrder(CreateNewOrderRequest createNewOrderRequest) {
        return Order.builder()
                .code(createNewOrderRequest.getCode())
                .fullName(createNewOrderRequest.getFullName())
                .phone(createNewOrderRequest.getPhone())
                .address(createNewOrderRequest.getAddress())
                .note(createNewOrderRequest.getNote())
                .totalPrice(createNewOrderRequest.getTotalPrice())
                .deliveryFee(createNewOrderRequest.getDeliveryFee())
                .status(EOrderStatus.WAITING)
                .orderDate(new Date())
                .deliveryMethod(createNewOrderRequest.getDeliveryMethod())
                .deliveryStatus(EDeliveryStatus.UNDELIVERED)
                .paymentMethod(createNewOrderRequest.getPaymentMethod())
                .paymentStatus(EPaymentStatus.UNPAID).build();
    }

    public OrderWithDetailResponse toOrderWithDetailResponse(Order order) {
        return OrderWithDetailResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .note(order.getNote())
                .totalPrice(order.getTotalPrice())
                .deliveryFee(order.getDeliveryFee())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .finishDate(order.getFinishDate())
                .deliveryMethod(order.getDeliveryMethod())
                .paymentMethod(order.getPaymentMethod())
                .deliveryStatus(order.getDeliveryStatus())
                .paymentStatus(order.getPaymentStatus())
                .orderDetails(orderDetailMapper.toListOrderDetailResponse(order.getOrderDetails())).build();
    }

    public OrderResponse toOrderResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .code(order.getCode())
                .fullName(order.getFullName())
                .phone(order.getPhone())
                .address(order.getAddress())
                .note(order.getNote())
                .totalPrice(order.getTotalPrice())
                .deliveryFee(order.getDeliveryFee())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .finishDate(order.getFinishDate())
                .deliveryMethod(order.getDeliveryMethod())
                .paymentMethod(order.getPaymentMethod())
                .deliveryStatus(order.getDeliveryStatus())
                .paymentStatus(order.getPaymentStatus()).build();
    }
}
