package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.order.VariantSizeRequest;
import com.khoahd7621.youngblack.dtos.response.order.OrderDetailResponse;
import com.khoahd7621.youngblack.entities.*;
import com.khoahd7621.youngblack.entities.composite.OrderDetailKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderDetailMapper {

    @Autowired
    private ColorMapper colorMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private SizeMapper sizeMapper;

    public OrderDetail toOrderDetail(VariantSizeRequest variantSizeRequest, VariantSize variantSize, Order order) {
        OrderDetailKey orderDetailKey = OrderDetailKey.builder()
                .orderId(order.getId())
                .variantSizeId(variantSizeRequest.getVariantSizeId()).build();
        return OrderDetail.builder()
                .orderDetailId(orderDetailKey)
                .order(order)
                .variantSize(variantSize)
                .price(variantSizeRequest.getPrice())
                .quantity(variantSizeRequest.getQuantity()).build();
    }

    public List<OrderDetailResponse> toListOrderDetailResponse(Set<OrderDetail> orderDetails) {
        return orderDetails.stream().map(item -> {
            VariantSize variantSize = item.getVariantSize();
            ProductVariant productVariant = variantSize.getProductVariant();
            Product product = productVariant.getProduct();
            return OrderDetailResponse.builder()
                    .name(product.getName())
                    .slug(product.getSlug())
                    .imageUrl(product.getPrimaryImageUrl())
                    .color(colorMapper.toColorResponse(productVariant.getColor()))
                    .size(sizeMapper.toSizeResponse(variantSize.getSize()))
                    .category(categoryMapper.toCategoryResponse(product.getCategory()))
                    .quantity(item.getQuantity())
                    .price(item.getPrice()).build();
        }).collect(Collectors.toList());
    }
}
