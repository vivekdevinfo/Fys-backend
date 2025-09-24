package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.dtos.request.order.CreateNewOrderRequest;
import com.khoahd7621.youngblack.dtos.request.order.VariantSizeRequest;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.order.CreateNewOrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.ListOrdersResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderWithDetailResponse;
import com.khoahd7621.youngblack.entities.Order;
import com.khoahd7621.youngblack.entities.OrderDetail;
import com.khoahd7621.youngblack.entities.User;
import com.khoahd7621.youngblack.entities.VariantSize;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.OrderDetailMapper;
import com.khoahd7621.youngblack.mappers.OrderMapper;
import com.khoahd7621.youngblack.repositories.OrderDetailRepository;
import com.khoahd7621.youngblack.repositories.OrderRepository;
import com.khoahd7621.youngblack.repositories.VariantSizeRepository;
import com.khoahd7621.youngblack.services.AuthService;
import com.khoahd7621.youngblack.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private AuthService authService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private VariantSizeRepository variantSizeRepository;
    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Override
    public SuccessResponse<CreateNewOrderResponse> createNewOrder(CreateNewOrderRequest createNewOrderRequest)
            throws NotFoundException {
        User user = authService.getUserLoggedIn();
        Order order = orderMapper.toOrder(createNewOrderRequest);
        order.setUser(user);
        Order orderDB = orderRepository.save(order);
        List<Long> listVariantSizeIds = createNewOrderRequest.getProducts().stream()
                .map(VariantSizeRequest::getVariantSizeId).collect(Collectors.toList());
        List<VariantSize> listVariantSizes = variantSizeRepository.findAllByIdIn(listVariantSizeIds);
        Map<Long, VariantSize> variantSizeMap = new HashMap<>();
        for (VariantSize variantSize : listVariantSizes) {
            variantSizeMap.put(variantSize.getId(), variantSize);
        }
        List<OrderDetail> orderDetails = createNewOrderRequest.getProducts().stream()
                .map(item -> orderDetailMapper.toOrderDetail(item, variantSizeMap.get(item.getVariantSizeId()), orderDB))
                .collect(Collectors.toList());
        orderDetailRepository.saveAll(orderDetails);
        return new SuccessResponse<>(CreateNewOrderResponse.builder().code(orderDB.getCode()).build(), "Order successfully.");
    }

    @Override
    public SuccessResponse<OrderWithDetailResponse> getOrderByCode(String code) throws NotFoundException, ForbiddenException {
        Optional<Order> orderOptional = orderRepository.findByCode(code);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Don't exist order with this code.");
        }
        User user = authService.getUserLoggedIn();
        User userOfOrder = orderOptional.get().getUser();
        if (userOfOrder.getId() != user.getId()) {
            throw new ForbiddenException("Don't have permission.");
        }
        OrderWithDetailResponse orderWithDetailResponse = orderMapper.toOrderWithDetailResponse(orderOptional.get());
        return new SuccessResponse<>(orderWithDetailResponse, "Get order successfully.");
    }

    @Override
    public SuccessResponse<ListOrdersResponse> getAllOrdersOfUser(long userId) throws NotFoundException, ForbiddenException {
        User user = authService.getUserLoggedIn();
        if (user.getId() != userId) {
            throw new ForbiddenException("Don't have permission.");
        }
        List<Order> orders = orderRepository.findAllByUserId(userId);
        List<OrderResponse> orderResponses = orders.stream()
                .map(orderMapper::toOrderResponse).collect(Collectors.toList());
        ListOrdersResponse listOrdersResponse = ListOrdersResponse.builder().orders(orderResponses).build();
        return new SuccessResponse<>(listOrdersResponse, "Get list orders successfully.");
    }

    @Override
    public SuccessResponse<OrderWithDetailResponse> getOrderWithDetail(Long orderId) throws NotFoundException, ForbiddenException {
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Don't exist order with this id.");
        }
        User userLoggedIn = authService.getUserLoggedIn();
        User user = orderOptional.get().getUser();
        if (user.getId() != userLoggedIn.getId()) {
            throw new ForbiddenException("Don't have permission.");
        }
        OrderWithDetailResponse orderWithDetailResponse = orderMapper.toOrderWithDetailResponse(orderOptional.get());
        return new SuccessResponse<>(orderWithDetailResponse, "Get order with detail success.");
    }
}
