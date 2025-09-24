package com.khoahd7621.youngblack.mappers;

import com.khoahd7621.youngblack.dtos.request.size.CreateNewSizeRequest;
import com.khoahd7621.youngblack.dtos.request.size.SizeRequest;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.entities.Size;
import org.springframework.stereotype.Component;

@Component
public class SizeMapper {

    public Size toSize(CreateNewSizeRequest createNewSizeRequest) {
        return Size.builder().size(createNewSizeRequest.getSize()).isDeleted(false).build();
    }

    public Size toSize(SizeRequest sizeRequest) {
        return Size.builder().id(sizeRequest.getId()).size(sizeRequest.getSize()).build();
    }


    public SizeResponse toSizeResponse(Size size) {
        return SizeResponse.builder().id(size.getId()).size(size.getSize()).build();
    }
}
