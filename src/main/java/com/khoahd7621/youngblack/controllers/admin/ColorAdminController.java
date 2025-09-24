package com.khoahd7621.youngblack.controllers.admin;

import com.khoahd7621.youngblack.dtos.request.color.CreateNewColorRequest;
import com.khoahd7621.youngblack.dtos.request.color.UpdateColorNameRequest;
import com.khoahd7621.youngblack.dtos.response.ExceptionResponse;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.color.ListColorsResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.services.ColorAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/color")
public class ColorAdminController {

    @Autowired
    private ColorAdminService colorAdminService;

    @Operation(summary = "Create new color")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create new color successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Color already existed.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PostMapping
    public SuccessResponse<ColorResponse> createNewColor(@Valid @RequestBody CreateNewColorRequest createNewColorRequest)
            throws BadRequestException {
        return colorAdminService.createNewColor(createNewColorRequest);
    }

    @Operation(summary = "Get all colors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get list colors successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @GetMapping
    private SuccessResponse<ListColorsResponse> getAllColors() {
        return colorAdminService.getAllColors();
    }

    @Operation(summary = "Update color")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update color successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Id of color is not existed. | New color is the same with old color. Nothing update. | This color already existed.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @PutMapping
    private SuccessResponse<ColorResponse> updateColorName(@Valid @RequestBody UpdateColorNameRequest updateColorNameRequest)
            throws BadRequestException {
        return colorAdminService.updateColorName(updateColorNameRequest);
    }

    @Operation(summary = "Delete color by color id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete color successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))}),
            @ApiResponse(responseCode = "400", description = "This color had products. Cannot delete.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))}),
            @ApiResponse(responseCode = "404", description = "Don't exist color with this id.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionResponse.class))})
    })
    @DeleteMapping("/{colorId}")
    public SuccessResponse<NoData> deleteColor(@PathVariable Integer colorId) throws NotFoundException, BadRequestException {
        return colorAdminService.deleteColor(colorId);
    }
}
