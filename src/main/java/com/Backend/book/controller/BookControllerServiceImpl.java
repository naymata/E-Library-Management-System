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

import static com.Backend.utility.ELibraryUtility.SOMETHING_WENT_WRONG;
import static org.springframework.http.HttpStatus.*;


@RestController
@RequiredArgsConstructor
public class BookControllerServiceImpl implements BookControllerService {

    private final BookService service;
    private static final Logger logger = LoggerFactory.getLogger(BookControllerServiceImpl.class);

    @Override
    public AddBookResponse addBook(AddBookRequest request) {
        try {
            return service.addBook(request);
        } catch (BookIsbnException | BookEanException | BookPagesException e) {
            return new AddBookResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new AddBookResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public BookDeleteResponse deleteBook(Long id) {
        try {
            return service.deleteBook(id);
        } catch (BookNotFoundException e) {
            return new BookDeleteResponse(HttpStatus.NOT_FOUND.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new BookDeleteResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }

    @Override
    public UpdateBookResponse updateBook(UpdateBookRequest request) {
        try {
            return service.updateBook(request);
        } catch (BookIsbnException | BookEanException | BookPagesException e) {
            return new UpdateBookResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new UpdateBookResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }


    @Override
    public BookPaginationResponse pagination(BookPaginationRequest request) {
        try {
            return service.pagination(request);
        } catch (BookListEmptyException e) {
            return new BookPaginationResponse(OK.value(), e.getMessage(), 0, new ArrayList<>());
        } catch (PaginationSizeException | PaginationPageException e) {
            return new BookPaginationResponse(BAD_REQUEST.value(), e.getMessage(), 0, new ArrayList<>());
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return new BookPaginationResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG, 0, new ArrayList<>());
        }
    }

    @Override
    public UpdateBookQuantityResponse updateBookQuantities(UpdateBookQuantityRequest request) {
        try {
            return service.updateBookQuantity(request);
        } catch (BookNotFoundException | BookQuantityException e) {
            return new UpdateBookQuantityResponse(BAD_REQUEST.value(), e.getMessage());
        } catch (RuntimeException e) {
            return new UpdateBookQuantityResponse(INTERNAL_SERVER_ERROR.value(), SOMETHING_WENT_WRONG);
        }
    }
}
