package com.khoahd7621.youngblack.controllers.admin;

import com.khoahd7621.youngblack.dtos.request.size.CreateNewSizeRequest;
import com.khoahd7621.youngblack.dtos.request.size.UpdateSizeRequest;
import com.khoahd7621.youngblack.dtos.response.ExceptionResponse;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.size.ListSizesResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.services.SizeAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/size")
public class SizeAdminController {

    @Autowired
    private SizeAdminService sizeAdminService;

    @Operation(summary = "Create new size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create new size successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Size already existed.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PostMapping
    public SuccessResponse<SizeResponse> createNewSize(@Valid @RequestBody CreateNewSizeRequest createNewSizeRequest)
            throws BadRequestException {
        return sizeAdminService.createNewSize(createNewSizeRequest);
    }

    @Operation(summary = "Get all sizes")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all sizes successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @GetMapping
    private SuccessResponse<ListSizesResponse> getAllSizes() {
        return sizeAdminService.getAllSizes();
    }

    @Operation(summary = "Update size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update size successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "Id of size is not existed. | New size is the same with old size. Nothing update. | Size already existed.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PutMapping
    private SuccessResponse<SizeResponse> updateSize(@Valid @RequestBody UpdateSizeRequest updateSizeRequest)
            throws BadRequestException {
        return sizeAdminService.updateSize(updateSizeRequest);
    }

    @Operation(summary = "Delete size")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete size successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "This size had products. Cannot delete.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist size with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @DeleteMapping("/{sizeId}")
    public SuccessResponse<NoData> deleteSize(@PathVariable Integer sizeId) throws NotFoundException, BadRequestException {
        return sizeAdminService.deleteSize(sizeId);
    }
}
