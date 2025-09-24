package com.khoahd7621.youngblack.services.impl;

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
import com.khoahd7621.youngblack.services.SizeAdminService;
import lombok.Builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Builder
public class SizeAdminServiceImpl implements SizeAdminService {

    @Autowired
    private SizeRepository sizeRepository;
    @Autowired
    private SizeMapper sizeMapper;

    @Override
    public SuccessResponse<SizeResponse> createNewSize(CreateNewSizeRequest createNewSizeRequest) throws BadRequestException {
        Optional<Size> sizeOptional = sizeRepository.findBySizeAndIsDeletedFalse(createNewSizeRequest.getSize());
        if (sizeOptional.isPresent()) {
            throw new BadRequestException("This size already existed.");
        }
        Size size = sizeMapper.toSize(createNewSizeRequest);
        Size result = sizeRepository.save(size);
        return new SuccessResponse<>(sizeMapper.toSizeResponse(result), "Create new size successfully.");
    }

    @Override
    public SuccessResponse<ListSizesResponse> getAllSizes() {
        List<Size> sizes = sizeRepository.findAllByIsDeletedFalse();
        List<SizeResponse> sizeResponseList = sizes.stream().map(sizeMapper::toSizeResponse).collect(Collectors.toList());
        ListSizesResponse listSizesResponse =
                ListSizesResponse.builder().sizes(sizeResponseList).build();
        return new SuccessResponse<>(listSizesResponse, "Get list size success.");
    }

    @Override
    public SuccessResponse<SizeResponse> updateSize(UpdateSizeRequest updateSizeRequest) throws BadRequestException {
        Optional<Size> sizeOptionalFindById = sizeRepository.findById(updateSizeRequest.getId());
        if (sizeOptionalFindById.isEmpty()) {
            throw new BadRequestException("Id of size is not exist.");
        }
        if (sizeOptionalFindById.get().getSize().equals(updateSizeRequest.getNewSize())) {
            throw new BadRequestException("New size is the same with old size. Nothing update.");
        }
        Optional<Size> sizeOptionalFindBySize = sizeRepository.findBySizeAndIsDeletedFalse(updateSizeRequest.getNewSize());
        if (sizeOptionalFindBySize.isPresent()) {
            throw new BadRequestException("This size already exist.");
        }
        Size size = sizeOptionalFindById.get();
        size.setSize(updateSizeRequest.getNewSize());
        sizeRepository.save(size);
        return new SuccessResponse<>(sizeMapper.toSizeResponse(size), "Update size success.");
    }

    @Override
    public SuccessResponse<NoData> deleteSize(int sizeId) throws NotFoundException, BadRequestException {
        Optional<Size> sizeOptional = sizeRepository.findById(sizeId);
        if (sizeOptional.isEmpty()) {
            throw new NotFoundException("Don't exist size with this id.");
        }
        Set<VariantSize> variantSizes = sizeOptional.get().getVariantSizes();
        if (variantSizes.size() > 0) {
            throw new BadRequestException("This size had products. Cannot delete!");
        }
        Size size = sizeOptional.get();
        size.setDeleted(true);
        sizeRepository.save(size);
        return new SuccessResponse<>(NoData.builder().build(), "Delete size successfully.");
    }
}
