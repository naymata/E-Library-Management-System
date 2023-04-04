package com.Backend.book.service;

import com.Backend.book.dto.BookRequest;
import com.Backend.book.dto.BookResponse;
import com.Backend.book.dto.BookUpdateRequest;
import com.Backend.book.model.Book;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface BookService {
    BookResponse addBook(@RequestBody BookRequest request);
    BookResponse deleteBook(@PathVariable Long id);
    BookResponse updateBook(BookUpdateRequest request);
    List<Book> getAllBooks();
}
