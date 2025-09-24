package com.khoahd7621.youngblack.controllers.user;

import com.khoahd7621.youngblack.dtos.request.order.CreateNewOrderRequest;
import com.khoahd7621.youngblack.dtos.response.ExceptionResponse;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.order.CreateNewOrderResponse;
import com.khoahd7621.youngblack.dtos.response.order.ListOrdersResponse;
import com.khoahd7621.youngblack.dtos.response.order.OrderWithDetailResponse;
import com.khoahd7621.youngblack.exceptions.ForbiddenException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "Create new order by user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @PostMapping
    public SuccessResponse<CreateNewOrderResponse> createNewOrder(@RequestBody CreateNewOrderRequest createNewOrderRequest)
            throws NotFoundException {
        return orderService.createNewOrder(createNewOrderRequest);
    }

    @Operation(summary = "Get order with detail by code")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get order with detail successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Don't have permission.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist order with this code.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping("/{code}")
    public SuccessResponse<OrderWithDetailResponse> getOrderByCode(@PathVariable String code)
            throws NotFoundException, ForbiddenException {
        return orderService.getOrderByCode(code);
    }

    @Operation(summary = "Get all orders of user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list orders successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Don't have permission.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping("/all")
    public SuccessResponse<ListOrdersResponse> getAllOrdersOfUser(@RequestParam(name = "user-id") Long userId)
            throws ForbiddenException, NotFoundException {
        return orderService.getAllOrdersOfUser(userId);
    }

    @Operation(summary = "Get order with detail by order id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get order with detail successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "403", description = "Don't have permission.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist order with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping("/detail")
    public SuccessResponse<OrderWithDetailResponse> getOrderWithDetail(@RequestParam(name = "order-id") Long orderId)
            throws ForbiddenException, NotFoundException {
        return orderService.getOrderWithDetail(orderId);
    }
}
