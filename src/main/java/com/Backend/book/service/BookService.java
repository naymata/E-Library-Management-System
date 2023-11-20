package com.Backend.book.service;

import com.Backend.book.dto.*;

public interface BookService {
    AddBookResponse addBook(AddBookRequest request);
    BookDeleteResponse deleteBook(Long id);
    UpdateBookResponse updateBook(UpdateBookRequest request);
    BookPaginationResponse pagination(BookPaginationRequest request);
    UpdateBookQuantityResponse updateBookQuantity(UpdateBookQuantityRequest request);
}
