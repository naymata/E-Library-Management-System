package com.Backend.book.implementation;

import com.Backend.book.dto.*;
import com.Backend.book.enums.OrderBy;
import com.Backend.book.exceptions.*;
import com.Backend.book.model.Book;
import com.Backend.book.model.Genres;
import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookService;
import com.Backend.config.utility.ELibraryUtility;
import com.Backend.notification.dto.AddNotificationRequest;
import com.Backend.notification.service.NotificationService;
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

import static com.Backend.config.utility.ELibraryUtility.*;
import static com.Backend.config.utility.ELibraryUtility.DELETED_SUCCESSFULLY;
import static com.Backend.config.utility.ELibraryUtility.UPDATED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.OK;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookRepository repository;
    private final NotificationService notificationService;
    private final String IS_TOO_SHORT = " is too short";
    private final String BOOK_EAN = "Book with EAN: ";
    private final String BOOK_ISBN = "Book with ISBN: ";
    private final String NOT_ONLY_DIGITS = " does not contain only digits";
    private final String BOOK_WITH_ID = "Book with id:";

    /**
     * Adds a new book to the system.
     *
     * @param request The request containing information about the book to be added.
     * @return An {@link AddBookResponse} indicating the result of the operation.
     */
    @Override
    public AddBookResponse addBook(AddBookRequest request) {
        exceptionHandling(request);
        var book = createBook(request);
        repository.save(book);
        return new AddBookResponse(OK.value(), "Book " + CREATED_SUCCESSFULLY);
    }

    /**
     * Deletes a book from the system based on the provided book ID.
     *
     * @param id The ID of the book to be deleted.
     * @return A {@link BookDeleteResponse} indicating the result of the operation.
     * @throws BookNotFoundException if no book with the specified ID is found.
     */
    @Override
    public BookDeleteResponse deleteBook(Long id) {
        if (!repository.existsById(id)) {
            throw new BookNotFoundException(stringFormatter(BOOK_WITH_ID, id, NOT_FOUND));
        }
        repository.deleteById(id);
        return new BookDeleteResponse(OK.value(), stringFormatter(BOOK_WITH_ID, id, DELETED_SUCCESSFULLY));
    }

    /**
     * Updates information about an existing book in the system.
     *
     * @param request The request containing updated information about the book.
     * @return An {@link UpdateBookResponse} indicating the result of the operation.
     * @throws BookNotFoundException if no book with the specified ID is found.
     */
    @Override
    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        var book = repository.findById(request.id()).orElseThrow(
                () -> new BookNotFoundException(stringFormatter(BOOK_WITH_ID, request.id(), NOT_FOUND))
        );
        exceptionHandling(request);
        repository.save(updateBook(book, request));
        return new UpdateBookResponse(OK.value(), stringFormatter(BOOK_WITH_ID, request.id(), UPDATED_SUCCESSFULLY));
    }

    /**
     * Retrieves a paginated list of books based on the specified criteria.
     *
     * @param request The request containing pagination and filtration criteria.
     * @return A {@link BookPaginationResponse} containing the paginated list of books.
     * @throws PaginationSizeException  if the specified page size is invalid.
     * @throws PaginationPageException  if the specified page number is invalid.
     * @throws OrderByException         if the specified order-by field is invalid.
     * @throws BookFieldException       if the specified book field for ordering is invalid.
     * @throws GenreFiltrationException if the specified genre for filtration is invalid.
     */
    @Override
    public BookPaginationResponse pagination(BookPaginationRequest request) {
        Pageable pageable;
        Page<Book> data;
        if (!checkPageSize(request.size())) {
            String INVALID_SIZE = "Invalid size";
            throw new PaginationSizeException(INVALID_SIZE);
        }
        if (request.page() < 0) {
            String INVALID_PAGE = "Invalid page";
            throw new PaginationPageException(INVALID_PAGE);
        }
        if (!isOrderAccurate(request)) {
            throw new OrderByException(stringFormatter("Order:", request.order(), "is ") + INVALID_INFORMATION);
        }
        if (!isBookFieldValid(request.field())) {
            throw new BookFieldException(stringFormatter("Field:", request.field(), "is ") + INVALID_INFORMATION);
        }
        if (request.order().equals(OrderBy.DESC)) {
            pageable = PageRequest.of(request.page(), request.size(), Sort.by(request.field()).descending());
        } else {
            pageable = PageRequest.of(request.page(), request.size(), Sort.by(request.field()).ascending());
        }
        if (!request.genres().equals(Genres.NONE)) {
            if (!isGenreAccurate(request)) {
                throw new GenreFiltrationException(stringFormatter("Genre:", request.genres(), "is ") + INVALID_INFORMATION);
            }
            data = repository.findByGenres(pageable, request.genres());
        } else {
            data = repository.findAll(pageable);
        }


        return new BookPaginationResponse(OK.value(), SUCCESS, data.getTotalPages() - 1, data.getContent());
    }

    /**
     * Updates the quantity of a book and triggers a notification if the quantity falls below 10.
     *
     * @param request The request containing the book ID and quantity to be updated.
     * @return An {@link UpdateBookQuantityResponse} indicating the result of the operation.
     * @throws BookNotFoundException if no book with the specified ID is found.
     * @throws BookQuantityException if the requested quantity exceeds the available quantity.
     */
    @Override
    public UpdateBookQuantityResponse updateBookQuantity(UpdateBookQuantityRequest request) {
        var book = repository.findById(request.bookId()).orElseThrow(() ->
                new BookNotFoundException(ELibraryUtility.stringFormatter(BOOK_WITH_ID, request.bookId(), NOT_FOUND)));
        var quantities = book.getQuantity();
        if (quantities < request.quantities()) {
            throw new BookQuantityException("Not enough quantities");
        }
        short updatedQuantities = (short) (quantities - request.quantities());
        if (updatedQuantities < 10) {
            var notificationRequest = new AddNotificationRequest(book.getId(), stringFormatter(BOOK_WITH_ID, book.getId(), "is low on quantities"));
            notificationService.addNotification(notificationRequest);
        }
        book.setQuantity(updatedQuantities);
        repository.save(book);
        return new UpdateBookQuantityResponse(OK.value(), "Book quantities " + UPDATED_SUCCESSFULLY);
    }

    /**
     * Creates a new book entity based on the information provided in the request.
     *
     * @param request The request containing information about the book.
     * @return A {@link Book} entity representing the new book.
     */
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

    /**
     * Updates the attributes of a book based on the information provided in the update request.
     *
     * @param book    The original book entity to be updated.
     * @param request The request containing updated information for the book.
     * @return The updated {@link Book} entity.
     */
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

    /**
     * Checks if the length of the provided string is equal to 13.
     *
     * @param s The string to be checked.
     * @return {@code true} if the length is 13, {@code false} otherwise.
     */
    private Boolean checkLength(String s) {
        return s.length() == 13;
    }

    /**
     * Checks if the provided string contains only digits.
     *
     * @param s The string to be checked.
     * @return {@code true} if the string contains only digits, {@code false} otherwise.
     */
    private Boolean checkIfOnlyDigit(String s) {
        return s.chars().allMatch(Character::isDigit);
    }

    /**
     * Checks if the provided integer is above zero.
     *
     * @param pages The integer to be checked.
     * @return {@code true} if the integer is above zero, {@code false} otherwise.
     */
    private Boolean checkIfIntegerIsAboveZero(Short pages) {
        return pages > 0;
    }

    /**
     * Checks if the specified order in the pagination request is accurate.
     *
     * @param request The pagination request.
     * @return {@code true} if the order is accurate, {@code false} otherwise.
     */
    private Boolean isOrderAccurate(BookPaginationRequest request) {
        var listOfOrders = List.of(OrderBy.ASC, OrderBy.DESC);
        return listOfOrders.contains(request.order());
    }

    /**
     * Checks if the specified genre in the pagination request is accurate.
     *
     * @param request The pagination request.
     * @return {@code true} if the genre is accurate, {@code false} otherwise.
     */
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

    /**
     * Handles exceptions related to book requests, such as ISBN and EAN validation.
     *
     * @param request The book request to be validated.
     * @param <T>     The type of the book request.
     * @throws BookIsbnException     if the ISBN is invalid.
     * @throws BookEanException      if the EAN is invalid.
     * @throws BookPagesException    if the number of pages is not above zero.
     * @throws BookQuantityException if the quantity is not above zero.
     */
    private <T extends BookRequest> void exceptionHandling(T request) {
        if (!checkLength(request.isbn())) {
            throw new BookIsbnException(stringFormatter(BOOK_ISBN, request.isbn(), IS_TOO_SHORT));
        }
        if (!checkLength(request.ean())) {
            throw new BookEanException(stringFormatter(BOOK_EAN, request.ean(), IS_TOO_SHORT));
        }
        if (!checkIfOnlyDigit(request.isbn())) {
            throw new BookIsbnException(stringFormatter(BOOK_ISBN, request.isbn(), NOT_ONLY_DIGITS));
        }
        if (!checkIfOnlyDigit(request.ean())) {
            throw new BookEanException(stringFormatter(BOOK_EAN, request.ean(), NOT_ONLY_DIGITS));
        }
        if (!checkIfIntegerIsAboveZero(request.pages())) {
            String BOOK_PAGES = "Book's pages are below 0";
            throw new BookPagesException(BOOK_PAGES);
        }
        if (!checkIfIntegerIsAboveZero(request.quantity())) {
            String BOOK_QUANTITY = "Book's quantity is below 0";
            throw new BookQuantityException(BOOK_QUANTITY);
        }
    }

    /**
     * Checks if the specified field is a valid attribute of the {@link Book} entity.
     *
     * @param field The field to be checked.
     * @return {@code true} if the field is valid, {@code false} otherwise.
     */
    private Boolean isBookFieldValid(String field) {
        List<String> fields = Arrays.stream(Book.class.getDeclaredFields())
                .map(Field::getName)
                .toList();
        return fields.contains(field);
    }
}
