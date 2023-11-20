package com.Backend.book.dto;

import com.Backend.book.model.Cover;
import com.Backend.book.model.Genres;

import java.math.BigDecimal;
import java.util.List;

public record AddBookRequest(
        String title,
        String author,
        Integer publishedOn,
        String publisher,
        BigDecimal price,
        List<Genres> genres,
        Cover cover,
        String ean,
        String isbn,
        Short pages,
        Short quantity,
        Boolean available
) implements BookRequest {

}
