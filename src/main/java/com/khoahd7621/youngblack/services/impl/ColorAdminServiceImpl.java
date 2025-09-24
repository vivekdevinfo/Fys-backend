package com.khoahd7621.youngblack.services.impl;

import com.khoahd7621.youngblack.dtos.request.color.CreateNewColorRequest;
import com.khoahd7621.youngblack.dtos.request.color.UpdateColorNameRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.color.ColorResponse;
import com.khoahd7621.youngblack.dtos.response.color.ListColorsResponse;
import com.khoahd7621.youngblack.entities.Color;
import com.khoahd7621.youngblack.entities.ProductVariant;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.ColorMapper;
import com.khoahd7621.youngblack.repositories.ColorRepository;
import com.khoahd7621.youngblack.services.ColorAdminService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Builder
public class ColorAdminServiceImpl implements ColorAdminService {

    @Autowired
    private ColorRepository colorRepository;
    @Autowired
    private ColorMapper colorMapper;

    @Override
    public SuccessResponse<ColorResponse> createNewColor(CreateNewColorRequest createNewColorRequest) throws BadRequestException {
        Optional<Color> colorOptional = colorRepository.findByNameAndIsDeletedFalse(createNewColorRequest.getName());
        if (colorOptional.isPresent()) {
            throw new BadRequestException("This color already existed.");
        }
        Color color = colorMapper.toColor(createNewColorRequest);
        Color result = colorRepository.save(color);
        return new SuccessResponse<>(colorMapper.toColorResponse(result), "Create new color successfully.");
    }

    @Override
    public SuccessResponse<ListColorsResponse> getAllColors() {
        List<Color> colorList = colorRepository.findAllByIsDeletedFalse();
        List<ColorResponse> sizeResponseList = colorList.stream().map(colorMapper::toColorResponse)
                .collect(Collectors.toList());
        ListColorsResponse listColorsResponse =
                ListColorsResponse.builder().colors(sizeResponseList).build();
        return new SuccessResponse<>(listColorsResponse, "Get list color success.");
    }

    @Override
    public SuccessResponse<ColorResponse> updateColorName(UpdateColorNameRequest updateColorNameRequest) throws BadRequestException {
        Optional<Color> colorOptionalFindById = colorRepository.findById(updateColorNameRequest.getId());
        if (colorOptionalFindById.isEmpty()) {
            throw new BadRequestException("Id of color is not exist.");
        }
        if (colorOptionalFindById.get().getName().equals(updateColorNameRequest.getNewName())) {
            throw new BadRequestException("New color is the same with old color. Nothing update.");
        }
        Optional<Color> colorOptionalFindByName = colorRepository.findByNameAndIsDeletedFalse(updateColorNameRequest.getNewName());
        if (colorOptionalFindByName.isPresent()) {
            throw new BadRequestException("This color already exist.");
        }
        Color color = colorOptionalFindById.get();
        color.setName(updateColorNameRequest.getNewName());
        colorRepository.save(color);
        return new SuccessResponse<>(colorMapper.toColorResponse(color), "Update name of color success.");
    }

    @Override
    public SuccessResponse<NoData> deleteColor(int colorId) throws BadRequestException, NotFoundException {
        Optional<Color> colorOptional = colorRepository.findById(colorId);
        if (colorOptional.isEmpty()) {
            throw new NotFoundException("Don't exist color with this id.");
        }
        Set<ProductVariant> productVariantSet = colorOptional.get().getProductVariants();
        if (productVariantSet.size() > 0) {
            throw new BadRequestException("This color had products. Cannot delete!");
        }
        Color color = colorOptional.get();
        color.setDeleted(true);
        colorRepository.save(color);
        return new SuccessResponse<>(NoData.builder().build(), "Delete color successfully.");
    }
}
