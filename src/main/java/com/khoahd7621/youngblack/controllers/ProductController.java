package com.khoahd7621.youngblack.controllers;

import com.khoahd7621.youngblack.dtos.response.ExceptionResponse;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductResponse;
import com.khoahd7621.youngblack.dtos.response.product.ListProductWithPaginateResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("/api/v1/product")
@Validated
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products with pagination and sort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list products successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @GetMapping("/all")
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsWithPaginateAndSort(
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "sort-base", defaultValue = "id") String sortBase,
            @RequestParam(name = "sort-type", defaultValue = "DESC") String sortType
    ) throws BadRequestException {
        return productService.getAllProductsWithPaginateAndSort(offset, limit, sortBase, sortType);
    }

    @Operation(summary = "Get all products by category name with pagination and sort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list products successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Don't exist category with this category name.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsByCategoryNameWithPaginateAndSort(
            @RequestParam(name = "category-name") String categoryName,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset,
            @RequestParam(name = "sort-base", defaultValue = "id") String sortBase,
            @RequestParam(name = "sort-type", defaultValue = "DESC") String sortType
    ) throws BadRequestException {
        return productService.getAllProductsByCategoryNameWithPaginateAndSort(categoryName, offset, limit, sortBase, sortType);
    }

    @Operation(summary = "Search all products by product name with pagination and sort")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list products successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @GetMapping("/search")
    public SuccessResponse<ListProductWithPaginateResponse> getAllProductsSearchByNameWithPaginate(
            @RequestParam(name = "query", defaultValue = "") String query,
            @RequestParam(name = "limit", defaultValue = "20") Integer limit,
            @RequestParam(name = "offset", defaultValue = "0") Integer offset
    ) {
        return productService.getAllProductsSearchByNameWithPaginate(query, offset, limit);
    }

    @Operation(summary = "Get n related products by category id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get related products successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist category with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @GetMapping("/related")
    public SuccessResponse<ListProductResponse> getNRelatedProductByCategoryId(
            @RequestParam(name = "category-id") @Min(value = 0, message = "Min number of category id is 0") Integer categoryId,
            @RequestParam(name = "number-elements") @Min(value = 1, message = "Min number of elements is 1") Integer numberElements
    ) throws NotFoundException {
        return productService.getNRelatedProductByCategoryId(categoryId, numberElements);
    }
}
