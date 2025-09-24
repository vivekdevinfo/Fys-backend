package com.khoahd7621.youngblack.services.impl;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import com.khoahd7621.youngblack.dtos.request.size.CreateNewSizeRequest;
import com.khoahd7621.youngblack.dtos.request.size.UpdateSizeRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.size.ListSizesResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.entities.Size;
import com.khoahd7621.youngblack.entities.VariantSize;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;
import com.khoahd7621.youngblack.mappers.SizeMapper;
import com.khoahd7621.youngblack.repositories.SizeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

class SizeAdminServiceImplTest {

    private SizeAdminServiceImpl sizeAdminServiceImpl;
    private SizeRepository sizeRepository;
    private SizeMapper sizeMapper;

    private Size size;
    private SizeResponse sizeResponse;

    @BeforeEach
    void beforeEach() {
        sizeRepository = mock(SizeRepository.class);
        sizeMapper = mock(SizeMapper.class);
        sizeAdminServiceImpl = SizeAdminServiceImpl.builder()
                .sizeRepository(sizeRepository)
                .sizeMapper(sizeMapper).build();
        size = mock(Size.class);
        sizeResponse = mock(SizeResponse.class);
    }

    @Test
    void createNewSize() {
    }

    @Test
    void createNewSize_ShouldThrowBadRequestException_WhenInValidDataRequest() {
        CreateNewSizeRequest dataRequest = CreateNewSizeRequest.builder().size("size").build();

        when(sizeRepository.findBySizeAndIsDeletedFalse(dataRequest.getSize())).thenReturn(Optional.of(size));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            sizeAdminServiceImpl.createNewSize(dataRequest);
        });

        assertThat(actual.getMessage(), is("This size already existed."));
    }

    @Test
    void createNewSize_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        CreateNewSizeRequest dataRequest = CreateNewSizeRequest.builder().size("size").build();
        SuccessResponse<SizeResponse> expected =
                new SuccessResponse<>(sizeResponse, "Create new size successfully.");

        when(sizeRepository.findBySizeAndIsDeletedFalse(dataRequest.getSize())).thenReturn(Optional.empty());
        when(sizeMapper.toSize(dataRequest)).thenReturn(size);
        when(sizeRepository.save(size)).thenReturn(size);
        when(sizeMapper.toSizeResponse(size)).thenReturn(sizeResponse);

        SuccessResponse<SizeResponse> actual = sizeAdminServiceImpl.createNewSize(dataRequest);

        verify(sizeRepository).save(size);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void getAllSizes_ShouldReturnData() {
        List<Size> sizes = new ArrayList<>();
        sizes.add(size);
        List<SizeResponse> sizeResponses = new ArrayList<>();
        sizeResponses.add(sizeResponse);
        ListSizesResponse listSizesResponse = ListSizesResponse.builder().sizes(sizeResponses).build();
        SuccessResponse<ListSizesResponse> expected =
                new SuccessResponse<>(listSizesResponse, "Get list size success.");

        when(sizeRepository.findAllByIsDeletedFalse()).thenReturn(sizes);
        when(sizeMapper.toSizeResponse(size)).thenReturn(sizeResponse);

        SuccessResponse<ListSizesResponse> actual = sizeAdminServiceImpl.getAllSizes();

        assertThat(actual.getData().getSizes(), is(expected.getData().getSizes()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void updateSize_ShouldThrowBadRequestException_WhenInvalidSizeIdInRequestData() {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).build();

        when(sizeRepository.findById(requestData.getId())).thenReturn(Optional.empty());

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            sizeAdminServiceImpl.updateSize(requestData);
        });

        assertThat(actual.getMessage(), is("Id of size is not exist."));
    }

    @Test
    void updateSize_ShouldThrowBadRequestException_WhenSizeInRequestDataTheSameWithSizeInDatabase() {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).newSize("size").build();

        when(sizeRepository.findById(requestData.getId())).thenReturn(Optional.of(size));
        when(Optional.of(size).get().getSize()).thenReturn("size");

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            sizeAdminServiceImpl.updateSize(requestData);
        });

        assertThat(actual.getMessage(), is("New size is the same with old size. Nothing update."));
    }

    @Test
    void updateSize_ShouldThrowBadRequestException_WhenSizeInRequestDataDuplicated() {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).newSize("size").build();

        when(sizeRepository.findById(requestData.getId())).thenReturn(Optional.of(size));
        when(Optional.of(size).get().getSize()).thenReturn("different-size");
        when(sizeRepository.findBySizeAndIsDeletedFalse(requestData.getNewSize())).thenReturn(Optional.of(size));

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            sizeAdminServiceImpl.updateSize(requestData);
        });

        assertThat(actual.getMessage(), is("This size already exist."));
    }

    @Test
    void updateSize_ShouldReturnData_WhenValidDataRequest() throws BadRequestException {
        UpdateSizeRequest requestData = UpdateSizeRequest.builder().id(1).newSize("size").build();
        SuccessResponse<SizeResponse> expected =
                new SuccessResponse<>(sizeResponse, "Update size success.");

        when(sizeRepository.findById(requestData.getId())).thenReturn(Optional.of(size));
        when(Optional.of(size).get().getSize()).thenReturn("different-size");
        when(sizeRepository.findBySizeAndIsDeletedFalse(requestData.getNewSize())).thenReturn(Optional.empty());
        when(sizeMapper.toSizeResponse(size)).thenReturn(sizeResponse);

        SuccessResponse<SizeResponse> actual = sizeAdminServiceImpl.updateSize(requestData);

        verify(sizeRepository).save(size);
        assertThat(actual.getData(), is(expected.getData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }

    @Test
    void deleteSize_ShouldThrowNotFoundException_WhenInValidDataRequest() {
        int sizeIdRequest = 1;

        when(sizeRepository.findById(sizeIdRequest)).thenReturn(Optional.empty());

        NotFoundException actual = assertThrows(NotFoundException.class, () -> {
            sizeAdminServiceImpl.deleteSize(sizeIdRequest);
        });

        assertThat(actual.getMessage(), is("Don't exist size with this id."));
    }

    @Test
    void deleteSize_ShouldThrowBadRequestException_WhenValidDataRequestButItHasProduct() {
        int sizeIdRequest = 1;
        VariantSize variantSize = mock(VariantSize.class);
        Set<VariantSize> variantSizes = new HashSet<>();
        variantSizes.add(variantSize);

        when(sizeRepository.findById(sizeIdRequest)).thenReturn(Optional.of(size));
        when(Optional.of(size).get().getVariantSizes()).thenReturn(variantSizes);

        BadRequestException actual = assertThrows(BadRequestException.class, () -> {
            sizeAdminServiceImpl.deleteSize(sizeIdRequest);
        });

        assertThat(actual.getMessage(), is("This size had products. Cannot delete!"));
    }

    @Test
    void deleteSize_ShouldThrowData_WhenValidDataRequest() throws NotFoundException, BadRequestException {
        int sizeIdRequest = 1;
        Set<VariantSize> variantSizes = new HashSet<>();
        SuccessResponse<NoData> expected = new SuccessResponse<>(NoData.builder().build(), "Delete size successfully.");

        when(sizeRepository.findById(sizeIdRequest)).thenReturn(Optional.of(size));
        when(Optional.of(size).get().getVariantSizes()).thenReturn(variantSizes);

        SuccessResponse<NoData> actual = sizeAdminServiceImpl.deleteSize(sizeIdRequest);

        verify(sizeRepository).save(size);
        assertThat(actual.getData().getNoData(), is(expected.getData().getNoData()));
        assertThat(actual.getCode(), is(expected.getCode()));
        assertThat(actual.getMessage(), is(expected.getMessage()));
    }
}