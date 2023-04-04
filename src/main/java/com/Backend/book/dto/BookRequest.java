package com.Backend.book.dto;

import java.math.BigDecimal;
import java.util.List;

public record BookRequest(
        String title,
        String author,
        Integer publishedOn,
        String publisher,
        BigDecimal price,
        List<String> genres
) {
}
