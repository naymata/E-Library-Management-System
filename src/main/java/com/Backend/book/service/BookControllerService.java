package com.Backend.book.service;

import com.Backend.book.dto.BookRequest;
import com.Backend.book.dto.BookResponse;
import com.Backend.book.dto.BookUpdateRequest;
import com.Backend.book.model.Book;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping(path = "/api/v1/book")
public interface BookControllerService {

    @PostMapping("/add-book")
    ResponseEntity<BookResponse> addBook(@RequestBody BookRequest request);


    @DeleteMapping("/delete/{id}")
    ResponseEntity<BookResponse> deleteBook(@PathVariable Long id);

    @PutMapping("/update")
    ResponseEntity<BookResponse> updateBook(@RequestBody BookUpdateRequest request);

    @GetMapping
    ResponseEntity<List<Book>> getBooks();
}
