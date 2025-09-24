package com.khoahd7621.youngblack.dtos.response.image;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ImageResponse {
    private long imageId;
    private String imageName;
    private String imageUrl;
}
