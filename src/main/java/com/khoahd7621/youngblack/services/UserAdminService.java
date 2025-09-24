package com.khoahd7621.youngblack.services;

import com.khoahd7621.youngblack.constants.EAccountStatus;
import com.khoahd7621.youngblack.constants.ERoles;
import com.khoahd7621.youngblack.dtos.request.user.CreateNewAdminUserRequest;
import com.khoahd7621.youngblack.dtos.response.NoData;
import com.khoahd7621.youngblack.dtos.response.SuccessResponse;
import com.khoahd7621.youngblack.dtos.response.user.ListUsersWithPaginateResponse;
import com.khoahd7621.youngblack.exceptions.BadRequestException;
import com.khoahd7621.youngblack.exceptions.NotFoundException;

public interface UserAdminService {

    public SuccessResponse<ListUsersWithPaginateResponse> getListUsersByRoleAndStatusWithPaginate(ERoles role, EAccountStatus status, Integer limit, Integer offset);

    public SuccessResponse<NoData> blockUserByUserId(long userId) throws NotFoundException, BadRequestException;

    public SuccessResponse<NoData> unBlockUserByUserId(long userId) throws NotFoundException, BadRequestException;

    public SuccessResponse<NoData> createNewAdminUser(CreateNewAdminUserRequest createNewAdminUserRequest) throws BadRequestException;
}
