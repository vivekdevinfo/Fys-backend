package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.color.ColorOfCreateNewProduct;
import com.khoahd7621.youngblack.dtos.request.color.CreateNewColorRequest;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.entities.Color;
import org.springframework.stereotype.Component;

@Component
public class ColorMapper {
    public Color toColor(CreateNewColorRequest createNewColorRequest) {
        return Color.builder().name(createNewColorRequest.getName()).isDeleted(false).build();
    }

    public Color toColor(ColorOfCreateNewProduct colorOfCreateNewProduct) {
        return Color.builder()
                .id(colorOfCreateNewProduct.getId())
                .name(colorOfCreateNewProduct.getName()).build();
    }

    public ColorResponse toColorResponse(Color color) {
        return ColorResponse.builder().id(color.getId()).name(color.getName()).build();
    }
}
