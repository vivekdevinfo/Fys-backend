package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.dtos.request.size.CreateNewSizeRequest;
import com.khoahd7621.youngblack.dtos.request.size.UpdateSizeRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.size.ListSizesResponse;
import com.khoahd7621.youngblack.dtos.response.size.SizeResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;

public interface SizeAdminService {
    public SuccessResponse<SizeResponse> createNewSize(CreateNewSizeRequest createNewSizeRequest) throws BadRequestException;

    public SuccessResponse<ListSizesResponse> getAllSizes();

    public SuccessResponse<SizeResponse> updateSize(UpdateSizeRequest updateSizeRequest) throws BadRequestException;

    public SuccessResponse<NoData> deleteSize(int sizeId) throws NotFoundException, BadRequestException;
}
