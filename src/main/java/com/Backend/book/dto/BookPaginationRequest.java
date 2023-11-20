package com.Backend.book.dto;


import com.Backend.book.enums.OrderBy;
import com.Backend.book.model.Genres;


public record BookPaginationRequest(
        Integer size,
        Integer page,
        OrderBy order,
        String field,
        Genres genres
) {
    public BookPaginationRequest {
        if (order == null) {
            order = OrderBy.ASC;
        }
        if (size == null) {
            size = 4;
        }
        if (page == null) {
            page = 0;
        }
        if (field == null) {
            field = "id";
        }
        if (genres == null) {
            genres = Genres.NONE;
        }
    }
}
