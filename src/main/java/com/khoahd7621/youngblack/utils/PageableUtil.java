package com.khoahd7621.youngblack.utils;

import com.khoahd7621.youngblack.exceptions.BadRequestException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

@Component
public class PageableUtil {

    public Pageable getPageable(int offset, int limit) {
        return PageRequest.of(offset, limit);
    }

    public Pageable getPageable(int offset, int limit, String fieldName, String sortType) throws BadRequestException {
        if (sortType.equals("ASC")) {
            return PageRequest.of(offset, limit, Sort.by(fieldName).ascending());
        }
        if (sortType.equals("DESC")) {
            return PageRequest.of(offset, limit, Sort.by(fieldName).descending());
        }
        throw new BadRequestException("Invalid sort type");
    }

}
