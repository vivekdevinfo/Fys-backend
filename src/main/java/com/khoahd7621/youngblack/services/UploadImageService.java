package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.image.UploadImageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadImageService {
    public SuccessResponse<UploadImageResponse> uploadSingleImage(MultipartFile multipartFile) throws IOException;

    public SuccessResponse<List<UploadImageResponse>> uploadMultiImages(List<MultipartFile> multipartFiles) throws IOException;
}
