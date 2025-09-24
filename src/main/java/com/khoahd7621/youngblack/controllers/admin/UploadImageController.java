package com.khoahd7621.youngblack.controllers.admin;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.image.UploadImageResponse;
import com.khoahd7621.youngblack.services.UploadImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/admin/image")
public class UploadImageController {

    @Autowired
    private UploadImageService uploadImageService;

    @Operation(summary = "Upload single image")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @PostMapping("/upload")
    public SuccessResponse<UploadImageResponse> uploadSingleImage(@RequestParam("file") MultipartFile multipartFile)
            throws IOException {
        return uploadImageService.uploadSingleImage(multipartFile);
    }

    @Operation(summary = "Upload multiple images")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Upload successfully.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = SuccessResponse.class))})
    })
    @PostMapping("/upload-multiple")
    public SuccessResponse<List<UploadImageResponse>> uploadMultiImages(@RequestParam("file") List<MultipartFile> multipartFiles)
            throws IOException {
        return uploadImageService.uploadMultiImages(multipartFiles);
    }

}
