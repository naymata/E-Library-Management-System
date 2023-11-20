package com.Backend.book.service;

import com.Backend.book.dto.*;
import org.springframework.web.bind.annotation.*;

@RequestMapping(path = "/api/v1.1/book")
public interface BookControllerService {

    @PostMapping("/add-book")
    AddBookResponse addBook(@RequestBody AddBookRequest request);
    @DeleteMapping("/delete/{id}")
    BookDeleteResponse deleteBook(@PathVariable Long id);
    @PutMapping("/update")
    UpdateBookResponse updateBook(@RequestBody UpdateBookRequest request);
    @GetMapping("/get-books")
    BookPaginationResponse pagination(@RequestBody BookPaginationRequest request);
    @PatchMapping("/book-quantity")
    UpdateBookQuantityResponse updateBookQuantities(@RequestBody UpdateBookQuantityRequest request);
}
