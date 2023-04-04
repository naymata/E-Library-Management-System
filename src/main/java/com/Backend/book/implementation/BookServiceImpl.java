package com.Backend.book.implementation;

import com.Backend.book.dto.BookRequest;
import com.Backend.book.dto.BookResponse;
import com.Backend.book.dto.BookUpdateRequest;
import com.Backend.book.exceptions.BookExceptions;
import com.Backend.book.model.Book;
import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookService;
import com.Backend.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;

    @Override
    public BookResponse addBook(BookRequest request) {
        try {
            var book = createBook(request);
            repository.save(book);
            return new BookResponse("Book "+ELibraryUtility.CREATED);
        } catch (Exception e) {
            throw new BookExceptions(e.getMessage(), e.getCause());
        }
    }

    @Override
    public BookResponse deleteBook(Long id) {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                return new BookResponse("Book " + ELibraryUtility.DELETED);
            }
            return new BookResponse(ELibraryUtility.INVALID_INFORMATION);
        } catch (Exception e) {
            throw new BookExceptions(e.getMessage(), e.getCause());
        }
    }

    @Override
    public BookResponse updateBook(BookUpdateRequest request) {
        try{
            var book = repository.findById(request.id()).orElseThrow();
            book.setTitle(request.title());
            book.setAuthor(request.author());
            book.setPublishedOn(request.publishedOn());
            book.setPublisher(request.publisher());
            book.setPrice(request.price());
            book.setGenres(request.genres());
            repository.save(book);
            return new BookResponse("Book"+ ELibraryUtility.UPDATED_SUCCESSFULLY);
        }catch (Exception e){
            throw new BookExceptions(e.getMessage(),e.getCause());
        }
    }

    @Override
    public List<Book> getAllBooks() {
        try {
            return repository.findAll();
        } catch (Exception e) {
            throw new BookExceptions(e.getMessage(), e.getCause());
        }
    }


    private Book createBook(BookRequest request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .publishedOn(request.publishedOn())
                .publisher(request.publisher())
                .price(request.price())
                .genres(request.genres())
                .build();
    }

}
