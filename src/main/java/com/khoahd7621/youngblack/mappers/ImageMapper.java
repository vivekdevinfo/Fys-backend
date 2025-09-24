package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.image.ImageOfCreateNewProduct;
import com.khoahd7621.youngblack.dtos.response.image.ImageResponse;
import com.khoahd7621.youngblack.entities.Image;
import com.khoahd7621.youngblack.entities.ProductVariant;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ImageMapper {
    public List<Image> toListImagesWithProductVariant(List<ImageOfCreateNewProduct> images, ProductVariant productVariant) {
        return images.stream().map((image) -> Image.builder()
                .name(image.getImageName())
                .imageUrl(image.getImageUrl())
                .productVariant(productVariant).build()).collect(Collectors.toList());
    }

    public ImageResponse toImageResponse(Image image) {
        return ImageResponse.builder()
                .imageId(image.getId())
                .imageName(image.getName())
                .imageUrl(image.getImageUrl()).build();
    }

    public List<ImageResponse> toListImageResponse(Set<Image> images) {
        return images.stream().map(this::toImageResponse).collect(Collectors.toList());
    }
}
