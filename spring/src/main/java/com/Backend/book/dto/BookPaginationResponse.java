package com.Backend.book.dto;

import com.Backend.book.model.Book;

import java.util.List;

public record BookPaginationResponse(
        Integer status,
        String message,
        Integer pages,
        List<Book> data
) {
}
