package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.request.order.CreateNewOrderRequest;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.order.CreateNewOrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.ListOrdersResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderWithDetailResponse;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;

public interface OrderService {
    public SuccessResponse<CreateNewOrderResponse> createNewOrder(CreateNewOrderRequest createNewOrderRequest) throws NotFoundException;

    public SuccessResponse<OrderWithDetailResponse> getOrderByCode(String code) throws NotFoundException, ForbiddenException;

    public SuccessResponse<ListOrdersResponse> getAllOrdersOfUser(long userId) throws NotFoundException, ForbiddenException;

    public SuccessResponse<OrderWithDetailResponse> getOrderWithDetail(Long orderId) throws NotFoundException, ForbiddenException;
}
