package com.Backend.book.dto;

public record UpdateBookQuantityRequest(
        Long bookId,
        Short quantities
) {
}
