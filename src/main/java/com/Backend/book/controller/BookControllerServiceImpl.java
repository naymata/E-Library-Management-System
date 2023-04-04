package com.Backend.book.controller;

import com.Backend.book.dto.BookRequest;
import com.Backend.book.dto.BookResponse;
import com.Backend.book.dto.BookUpdateRequest;
import com.Backend.book.model.Book;
import com.Backend.book.service.BookControllerService;
import com.Backend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class BookControllerServiceImpl implements BookControllerService {

    private final BookService service;

    @Override
    public ResponseEntity<BookResponse> addBook(BookRequest request) {
        return ResponseEntity.ok(service.addBook(request));
    }

    @Override
    public ResponseEntity<BookResponse> deleteBook(Long id) {
        return ResponseEntity.ok(service.deleteBook(id));
    }

    @Override
    public ResponseEntity<BookResponse> updateBook(BookUpdateRequest request) {
        return ResponseEntity.ok(service.updateBook(request));
    }

    @Override
    public ResponseEntity<List<Book>> getBooks() {
        return ResponseEntity.ok(service.getAllBooks());
    }
}
