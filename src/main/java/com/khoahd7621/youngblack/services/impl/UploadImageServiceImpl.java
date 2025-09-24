package com.khoahd7621.youngblack.services.impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.image.UploadImageResponse;
import com.khoahd7621.youngblack.services.UploadImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class UploadImageServiceImpl implements UploadImageService {

    @Autowired
    private Cloudinary cloudinary;

    private static final String FOLDER_NAME = "youngblack-products";
    private static final String[] ALLOWED_FORMATS = {"jpg", "jpeg", "png", "gif", "webp"};
    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB

    @Override
    public SuccessResponse<UploadImageResponse> uploadSingleImage(MultipartFile multipartFile) throws IOException {
        validateFile(multipartFile);
        UploadImageResponse uploadImageResponse = uploadImage(multipartFile);
        return new SuccessResponse<>(uploadImageResponse, "Successfully uploaded.");
    }

    @Override
    public SuccessResponse<List<UploadImageResponse>> uploadMultiImages(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadImageResponse> uploadImageResponseList = new ArrayList<>();
        
        for (MultipartFile multipartFile : multipartFiles) {
            if (multipartFile != null && !multipartFile.isEmpty()) {
                validateFile(multipartFile);
                UploadImageResponse uploadImageResponse = uploadImage(multipartFile);
                uploadImageResponseList.add(uploadImageResponse);
            }
        }
        
        return new SuccessResponse<>(uploadImageResponseList, "Successfully uploaded.");
    }

    private UploadImageResponse uploadImage(MultipartFile multipartFile) throws IOException {
        try {
            String originalFileName = multipartFile.getOriginalFilename();
            String fileExtension = getExtension(originalFileName);
            String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

            // Upload parameters for Cloudinary
            Map<String, Object> uploadParams = ObjectUtils.asMap(
                "folder", FOLDER_NAME,
                "public_id", uniqueFileName.substring(0, uniqueFileName.lastIndexOf('.')), // Remove extension for public_id
                "resource_type", "image",
                "format", getFormatFromExtension(fileExtension),
                "transformation", new Transformation()
                    .quality("auto")
                    .fetchFormat("auto"),
                "tags", "youngblack,product"
            );

            // Upload to Cloudinary
            Map<String, Object> uploadResult = cloudinary.uploader().upload(multipartFile.getBytes(), uploadParams);

            // Extract results
            String imageUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            log.info("Successfully uploaded image to Cloudinary: {}", publicId);

            return UploadImageResponse.builder()
                .imageName(publicId)
                .imageUrl(imageUrl)
                .build();

        } catch (IOException e) {
            log.error("Error uploading image to Cloudinary: ", e);
            throw new IOException("Failed to upload image to Cloudinary", e);
        }
    }

    private void validateFile(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IOException("File is empty or null");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IOException("File size exceeds maximum limit of 10MB");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || !isValidImageFormat(originalFileName)) {
            throw new IOException("Invalid file format. Only JPG, JPEG, PNG, GIF, and WEBP are allowed");
        }

        // Validate content type
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IOException("File must be an image");
        }
    }

    private boolean isValidImageFormat(String fileName) {
        String extension = getExtension(fileName).toLowerCase().substring(1); // Remove the dot
        for (String allowedFormat : ALLOWED_FORMATS) {
            if (allowedFormat.equals(extension)) {
                return true;
            }
        }
        return false;
    }

    private String getExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return ".jpg"; // Default extension
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private String getFormatFromExtension(String extension) {
        return extension.substring(1).toLowerCase(); // Remove dot and convert to lowercase
    }

    /**
     * Delete image from Cloudinary
     * @param publicId The public ID of the image to delete
     * @return true if deletion was successful
     */
    public boolean deleteImage(String publicId) {
        try {
            Map<String, Object> result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            String resultStatus = (String) result.get("result");
            
            if ("ok".equals(resultStatus)) {
                log.info("Successfully deleted image from Cloudinary: {}", publicId);
                return true;
            } else {
                log.warn("Failed to delete image from Cloudinary: {} - Status: {}", publicId, resultStatus);
                return false;
            }
        } catch (IOException e) {
            log.error("Error deleting image from Cloudinary: {}", publicId, e);
            return false;
        }
    }

    /**
     * Generate transformed image URL
     * @param publicId The public ID of the image
     * @param width Desired width
     * @param height Desired height
     * @param crop Crop mode (e.g., "fill", "fit", "limit")
     * @return Transformed image URL
     */
    public String generateTransformedUrl(String publicId, int width, int height, String crop) {
        return cloudinary.url()
            .transformation(new Transformation()
                .width(width)
                .height(height)
                .crop(crop)
                .quality("auto")
                .fetchFormat("auto"))
            .generate(publicId);
    }

    /**
     * Generate thumbnail URL
     * @param publicId The public ID of the image
     * @return Thumbnail URL (150x150)
     */
    public String generateThumbnailUrl(String publicId) {
        return generateTransformedUrl(publicId, 150, 150, "fill");
    }
}