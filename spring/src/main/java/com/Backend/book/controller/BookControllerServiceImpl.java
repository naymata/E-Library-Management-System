package com.Backend.book.controller;

import com.Backend.book.dto.*;
import com.Backend.book.exceptions.*;
import com.Backend.book.service.BookControllerService;
import com.Backend.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

import static com.Backend.config.utility.ELibraryUtility.SOMETHING_WENT_WRONG;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
public class BookControllerServiceImpl implements BookControllerService {

    private final BookService service;
    private static final Logger logger = LoggerFactory.getLogger(BookControllerServiceImpl.class);

    /**
     * Adds a new book based on the information provided in the {@link AddBookRequest}.
     *
     * @param request The {@link AddBookRequest} containing details of the book to be added.
     * @return An {@link AddBookResponse} indicating the result of the addition operation.
     * @throws BookIsbnException  if the ISBN of the book is invalid.
     * @throws BookEanException   if the EAN of the book is invalid.
     * @throws BookPagesException if the number of pages in the book is invalid.
     */
    @Override
    public AddBookResponse addBook(AddBookRequest request) {
        try {
            return service.addBook(request);
        } catch (BookIsbnException | BookEanException | BookPagesException e) {
            return new AddBookResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            return new AddBookResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Deletes a book with the specified ID.
     *
     * @param id The ID of the book to be deleted.
     * @return A {@link BookDeleteResponse} indicating the result of the deletion operation.
     * @throws BookNotFoundException if the book with the specified ID is not found.
     */
    @Override
    public BookDeleteResponse deleteBook(Long id) {
        try {
            return service.deleteBook(id);
        } catch (BookNotFoundException e) {
            return new BookDeleteResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            return new BookDeleteResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

    /**
     * Updates the information of a book based on the provided {@link UpdateBookRequest}.
     *
     * @param request The {@link UpdateBookRequest} containing updated book information.
     * @return An {@link UpdateBookResponse} indicating the result of the update operation.
     * @throws BookIsbnException  if the updated ISBN is invalid.
     * @throws BookEanException   if the updated EAN is invalid.
     * @throws BookPagesException if the updated number of pages is invalid.
     */
    @Override
    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        try {
            return service.updateBook(request);
        } catch (BookIsbnException | BookEanException | BookPagesException e) {
            return new UpdateBookResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            return new UpdateBookResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }


    /**
     * Retrieves a paginated list of books based on the provided {@link BookPaginationRequest}.
     *
     * @param request The {@link BookPaginationRequest} specifying pagination parameters.
     * @return A {@link BookPaginationResponse} containing paginated book data.
     * @throws BookListEmptyException  if the paginated result is empty.
     * @throws PaginationSizeException if the pagination size is invalid.
     * @throws PaginationPageException if the pagination page is invalid.
     */
    @Override
    public BookPaginationResponse pagination(BookPaginationRequest request) {
        try {
            return service.pagination(request);
        } catch (BookListEmptyException e) {
            return new BookPaginationResponse(OK.value(), e.getMessage(), 0, new ArrayList<>());
        } catch (PaginationSizeException | PaginationPageException e) {
            return new BookPaginationResponse(BAD_REQUEST.value(), e.getMessage(), 0, new ArrayList<>());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            return new BookPaginationResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG, 0, new ArrayList<>());
        }
    }

    /**
     * Updates the quantities of books based on the provided {@link UpdateBookQuantityRequest}.
     *
     * @param request The {@link UpdateBookQuantityRequest} containing updated book quantities.
     * @return An {@link UpdateBookQuantityResponse} indicating the result of the update operation.
     * @throws BookNotFoundException if a book with the specified ID is not found.
     * @throws BookQuantityException if the updated quantity is invalid.
     */
    @Override
    public UpdateBookQuantityResponse updateBookQuantities(UpdateBookQuantityRequest request) {
        try {
            return service.updateBookQuantity(request);
        } catch (BookNotFoundException | BookQuantityException e) {
            return new UpdateBookQuantityResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage(), e.fillInStackTrace());
            return new UpdateBookQuantityResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }
}
