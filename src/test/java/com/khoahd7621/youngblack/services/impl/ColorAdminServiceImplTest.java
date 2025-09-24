package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class ColorAdminServiceImplTest {

    private ColorAdminServiceImpl colorAdminServiceImpl;
    private ColorRepository colorRepository;
    private ColorMapper colorMapper;
    private Color color;
    private ColorResponse colorResponse;

    @BeforeEach
    void beforeEach() {
        colorRepository = mock(ColorRepository.class);
        colorMapper = mock(ColorMapper.class);
        colorAdminServiceImpl = ColorAdminServiceImpl.builder()
                .colorRepository(colorRepository)
                .colorMapper(colorMapper).build();
        color = mock(Color.class);
        colorResponse = mock(ColorResponse.class);
    }

    @Test
    void createNewColor_ShouldThrowBadRequestException_WhenInValidDataRequest() {
        CreateNewColorRequest dataRequest = CreateNewColorRequest.builder().name("name").build();

        when(colorRepository.findByNameAndIsDeletedFalse(dataRequest.getName())).thenReturn(Optional.of(color));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            colorAdminServiceImpl.createNewColor(dataRequest);
        });

        assertThat(actual.getMessage(), is("This color already existed."));
    }

    @Test
    void createNewColor_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        CreateNewColorRequest dataRequest = CreateNewColorRequest.builder().name("name").build();
        SuccessResponse<ColorResponse> expected = new SuccessResponse<>(colorResponse, "Create new color successfully.");

        when(colorRepository.findByNameAndIsDeletedFalse(dataRequest.getName())).thenReturn(Optional.empty());
        when(colorMapper.toColor(dataRequest)).thenReturn(color);
        when(colorRepository.save(color)).thenReturn(color);
        when(colorMapper.toColorResponse(color)).thenReturn(colorResponse);

        SuccessResponse<ColorResponse> actual = colorAdminServiceImpl.createNewColor(dataRequest);

        verify(colorRepository).save(color);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void getAllColors_ShouldReturnData() {
        List<Color> colors = new ArrayList<>();
        colors.add(color);
        List<ColorResponse> colorResponses = new ArrayList<>();
        colorResponses.add(colorResponse);
        ListColorsResponse listColorsResponse = ListColorsResponse.builder().colors(colorResponses).build();
        SuccessResponse<ListColorsResponse> expected =
                new SuccessResponse<>(listColorsResponse, "Get list color success.");

        when(colorRepository.findAllByIsDeletedFalse()).thenReturn(colors);
        when(colorMapper.toColorResponse(color)).thenReturn(colorResponse);

        SuccessResponse<ListColorsResponse> actual = colorAdminServiceImpl.getAllColors();

        assertThat(actual.getData().getColors(), is(expected.getData().getColors()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void updateColorName_ShouldThrowBadRequestException_WhenInvalidColorIdInRequestData() {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).build();

        when(colorRepository.findById(requestData.getId())).thenReturn(Optional.empty());

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            colorAdminServiceImpl.updateColorName(requestData);
        });

        assertThat(actual.getMessage(), is("Id of color is not exist."));
    }

    @Test
    void updateColorName_ShouldThrowBadRequestException_WhenNameOfColorInRequestDataTheSameWithColorNameInDatabase() {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).newName("name").build();

        when(colorRepository.findById(requestData.getId())).thenReturn(Optional.of(color));
        when(Optional.of(color).get().getName()).thenReturn("name");

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            colorAdminServiceImpl.updateColorName(requestData);
        });

        assertThat(actual.getMessage(), is("New color is the same with old color. Nothing update."));
    }

    @Test
    void updateColorName_ShouldThrowBadRequestException_WhenNameOfColorInRequestDataDuplicated() {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).newName("name").build();

        when(colorRepository.findById(requestData.getId())).thenReturn(Optional.of(color));
        when(Optional.of(color).get().getName()).thenReturn("different-name");
        when(colorRepository.findByNameAndIsDeletedFalse(requestData.getNewName())).thenReturn(Optional.of(color));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            colorAdminServiceImpl.updateColorName(requestData);
        });

        assertThat(actual.getMessage(), is("This color already exist."));
    }

    @Test
    void updateColorName_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        UpdateColorNameRequest requestData = UpdateColorNameRequest.builder().id(1).newName("name").build();
        SuccessResponse<ColorResponse> expected =
                new SuccessResponse<>(colorResponse, "Update name of color success.");

        when(colorRepository.findById(requestData.getId())).thenReturn(Optional.of(color));
        when(Optional.of(color).get().getName()).thenReturn("different-name");
        when(colorRepository.findByNameAndIsDeletedFalse(requestData.getNewName())).thenReturn(Optional.empty());
        when(colorMapper.toColorResponse(color)).thenReturn(colorResponse);

        SuccessResponse<ColorResponse> actual = colorAdminServiceImpl.updateColorName(requestData);

        verify(colorRepository).save(color);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void deleteColor_ShouldThrowNotFoundException_WhenInValidDataRequest() {
        int colorIdRequest = 1;

        when(colorRepository.findById(colorIdRequest)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            colorAdminServiceImpl.deleteColor(colorIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't exist color with this id."));
    }

    @Test
    void deleteColor_ShouldThrowBadRequestException_WhenValidDataRequestButItHasProduct() {
        int colorIdRequest = 1;
        ProductVariant productVariant = mock(ProductVariant.class);
        Set<ProductVariant> productVariantSet = new HashSet<>();
        productVariantSet.add(productVariant);

        when(colorRepository.findById(colorIdRequest)).thenReturn(Optional.of(color));
        when(Optional.of(color).get().getProductVariants()).thenReturn(productVariantSet);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            colorAdminServiceImpl.deleteColor(colorIdRequest);
        });

        assertThat(actual.getMessage(), is("This color had products. Cannot delete!"));
    }

    @Test
    void deleteColor_ShouldThrowData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        int colorIdRequest = 1;
        Set<ProductVariant> productVariantSet = new HashSet<>();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "Delete color successfully.");

        when(colorRepository.findById(colorIdRequest)).thenReturn(Optional.of(color));
        when(Optional.of(color).get().getProductVariants()).thenReturn(productVariantSet);

        SuccessResponse<NoData> actual = colorAdminServiceImpl.deleteColor(colorIdRequest);

        verify(colorRepository).save(color);
        assertThat(actual.getData().getNoData(), is(expected.getData().getNoData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}