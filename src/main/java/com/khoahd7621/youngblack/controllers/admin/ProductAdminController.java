package com.khoahd7621.youngblack.controllers.admin;

import com.khoahd7621.youngblack.dtos.request.product.CreateNewProductRequest;
import com.khoahd7621.youngblack.dtos.request.product.UpdateProductRequest;
import com.khoahd7621.youngblack.dtos.response.ExceptionResponse;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductAdminWithPaginateResponse;
import com.khoahd7621.youngblack.dtos.response.productdetail.ProductDetailAdminResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.services.ProductAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/product")
public class ProductAdminController {

    @Autowired
    private ProductAdminService productAdminService;

    @Operation(summary = "Create new product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create new product successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Product name already existed. | Sku already existed.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PostMapping
    public SuccessResponse<NoData> createNewProduct(@Valid @RequestBody CreateNewProductRequest createNewProductRequest)
            throws BadRequestException {
        return productAdminService.createNewProduct(createNewProductRequest);
    }

    @Operation(summary = "Get all product with pagination")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list products successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @GetMapping
    public SuccessResponse<ListProductAdminWithPaginateResponse> getAllProductWithPaginate(
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset
    ) {
        return productAdminService.getAllProductWithPaginate(limit, offset);
    }

    @Operation(summary = "Get product detail by product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get product detail successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist product with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping("/{productId}")
    public SuccessResponse<ProductDetailAdminResponse> getProductDetailByProductId(@PathVariable Integer productId)
            throws NotFoundException {
        return productAdminService.getProductDetailByProductId(productId);
    }

    @Operation(summary = "Delete product detail by product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete product successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist product with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @DeleteMapping("/{productId}")
    public SuccessResponse<NoData> deleteProductByProductId(@PathVariable Integer productId) throws NotFoundException {
        return productAdminService.deleteProductByProductId(productId);
    }

    @Operation(summary = "Update product detail by product id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update product successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Discount price must be lower than price. | Start date discount is required when discount price greater than 0. | Start date discount must after today. | End date discount is required when discount price greater than 0. | Start date discount must before end date discount. | Don't exist product with this id. | Product name already exist. | Don't exist category with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PutMapping("/{productId}")
    public SuccessResponse<NoData> updateProductByProductId(
            @PathVariable Integer productId,
            @Valid @RequestBody UpdateProductRequest updateProductRequest) throws NotFoundException, BadRequestException {
        return productAdminService.updateProductByProductId(productId, updateProductRequest);
    }
}
