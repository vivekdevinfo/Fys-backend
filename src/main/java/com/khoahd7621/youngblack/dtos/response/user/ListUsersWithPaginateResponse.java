package com.khoahd7621.youngblack.dtos.response.user;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class ListUsersWithPaginateResponse {
    private long totalRows;
    private int totalPages;
    private List<UserResponse> listUsers;
}
