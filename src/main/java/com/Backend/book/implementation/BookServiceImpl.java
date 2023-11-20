package com.Backend.book.implementation;

import com.Backend.book.dto.*;
import com.Backend.book.enums.OrderBy;
import com.Backend.book.exceptions.*;
import com.Backend.book.model.Book;
import com.Backend.book.model.Genres;
import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookService;
import com.Backend.utility.ELibraryUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.Backend.utility.ELibraryUtility.*;
import static com.Backend.utility.ELibraryUtility.DELETED_SUCCESSFULLY;
import static com.Backend.utility.ELibraryUtility.UPDATED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private static final String IS_TOO_SHORT = " is too short";
    private static final String BOOK_EAN = "Book with EAN: ";
    private static final String BOOK_ISBN = "Book with ISBN: ";
    private static final String NOT_ONLY_DIGITS = " does not contain only digits";
    private static final String BOOK_PAGES = "Book's pages are below 0";
    private static final String BOOK_QUANTITY = "Book's quantity is below 0";
    private static final String INVALID_PAGE = "Invalid page";
    private static final String INVALID_SIZE = "Invalid size";

    @Override
    public AddBookResponse addBook(AddBookRequest request) {
        exceptionHandling(request);
        var book = createBook(request);
        repository.save(book);
        return new AddBookResponse(OK.value(), "Book " + CREATED_SUCCESSFULLY);
    }

    @Override
    public BookDeleteResponse deleteBook(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(bookWithIdWithMessage(id, NOT_FOUND));
        }
        repository.deleteById(id);
        return new BookDeleteResponse(OK.value(), bookWithIdWithMessage(id, DELETED_SUCCESSFULLY));
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        var book = repository.findById(request.id()).orElseThrow(
                () -> new BookNotFoundException(bookWithIdWithMessage(request.id(), NOT_FOUND))
        );
        exceptionHandling(request);
        repository.save(updateBook(book, request));
        return new UpdateBookResponse(OK.value(), bookWithIdWithMessage(request.id(), UPDATED_SUCCESSFULLY));
    }


    @Override
    public BookPaginationResponse pagination(BookPaginationRequest request) {
        Pageable pageable;
        Page<Book> data;
        if (!checkSize(request.size())) {
            throw new PaginationSizeException(INVALID_SIZE);
        }
        if (request.page() < 0) {
            throw new PaginationPageException(INVALID_PAGE);
        }
        if (!isOrderAccurate(request)) {
            throw new OrderByException("Order: " + request.order() + " is invalid");
        }
        if (!isBookFieldValid(request.field())) {
            throw new BookFieldException("Field: " + request.field() + " is " + INVALID_INFORMATION);
        }
        if (request.order().equals(OrderBy.DESC)) {
            pageable = PageRequest.of(request.page(), request.size(), Sort.by(request.field()).descending());
        } else {
            pageable = PageRequest.of(request.page(), request.size(), Sort.by(request.field()).ascending());
        }
        if (!request.genres().equals(Genres.NONE)) {
            if (!isGenreAccurate(request)) {
                throw new GenreFiltrationException("Genre " + request.genres() + " is invalid");
            }
            data = repository.findByGenres(pageable, request.genres());
        } else {
            data = repository.findAll(pageable);
        }


        return new BookPaginationResponse(OK.value(), SUCCESS, data.getTotalPages() - 1, data.getContent());
    }

    @Override
    public UpdateBookQuantityResponse updateBookQuantity(UpdateBookQuantityRequest request) {
        var book = repository.findById(request.bookId()).orElseThrow(() ->
                new BookNotFoundException(bookWithIdWithMessage(request.bookId(), NOT_FOUND)));
        var quantities = book.getQuantity();
        if (quantities < request.quantities()) {
            throw new BookQuantityException("Not enough quantities");
        }
        book.setQuantity((short) (quantities - request.quantities()));
        repository.save(book);
        return new UpdateBookQuantityResponse(OK.value(), "Book quantities " + UPDATED_SUCCESSFULLY);
    }

    private Book createBook(AddBookRequest request) {
        return Book.builder()
                .title(request.title())
                .author(request.author())
                .publishedOn(request.publishedOn())
                .publisher(request.publisher())
                .price(request.price())
                .genres(request.genres())
                .cover(request.cover())
                .ean(request.ean())
                .isbn(request.isbn())
                .pages(request.pages())
                .quantity(request.quantity())
                .isAvailable(request.available())
                .addedOn(LocalDate.now())
                .build();
    }

    private Book updateBook(Book book, UpdateBookRequest request) {
        book.setTitle(request.title());
        book.setAuthor(request.author());
        book.setPublishedOn(request.publishedOn());
        book.setPrice(request.price());
        book.setGenres(request.genres());
        book.setCover(request.cover());
        book.setEan(request.ean());
        book.setIsbn(request.isbn());
        book.setPages(request.pages());
        book.setQuantity(request.quantity());
        book.setIsAvailable(request.available());
        return book;
    }

    private String bookWithId(Long id) {
        return String.format("Book with id: %s", id);
    }

    private String bookWithIdWithMessage(Long id, String s) {
        return bookWithId(id) + " " + s;
    }

    private Boolean checkLength(String s) {
        return s.length() == 13;
    }

    private Boolean checkIfOnlyDigit(String s) {
        return s.chars().allMatch(Character::isDigit);
    }

    private Boolean checkIfIntegerIsAboveZero(Short pages) {
        return pages > 0;
    }

    private Boolean checkSize(Integer size) {
        Integer[] sizes = {4, 8, 12, 24};
        return Arrays.asList(sizes).contains(size);
    }

    private Boolean isOrderAccurate(BookPaginationRequest request) {
        var listOfOrders = List.of(OrderBy.ASC, OrderBy.DESC);
        return listOfOrders.contains(request.order());
    }

    private Boolean isGenreAccurate(BookPaginationRequest request) {
        var genresList = Genres.values();
        var list = new ArrayList<>();
        for (var i : genresList) {
            if (i.equals(Genres.NONE) || i.equals(Genres.TESTING)) {
                continue;
            }
            list.add(i);
        }
        return list.contains(request.genres());
    }

    private <T extends BookRequest> void exceptionHandling(T request) {
        if (!checkLength(request.isbn())) {
            throw new BookIsbnException(BOOK_ISBN + request.isbn() + IS_TOO_SHORT);
        }
        if (!checkLength(request.ean())) {
            throw new BookEanException(BOOK_EAN + request.ean() + IS_TOO_SHORT);
        }
        if (!checkIfOnlyDigit(request.isbn())) {
            throw new BookIsbnException(BOOK_ISBN + request.isbn() + NOT_ONLY_DIGITS);
        }
        if (!checkIfOnlyDigit(request.ean())) {
            throw new BookEanException(BOOK_EAN + request.ean() + NOT_ONLY_DIGITS);
        }
        if (!checkIfIntegerIsAboveZero(request.pages())) {
            throw new BookPagesException(BOOK_PAGES);
        }
        if (!checkIfIntegerIsAboveZero(request.quantity())) {
            throw new BookQuantityException(BOOK_QUANTITY);
        }
    }

    private Boolean isBookFieldValid(String field) {
        List<String> fields = Arrays.stream(Book.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        return fields.contains(field);
    }
}
