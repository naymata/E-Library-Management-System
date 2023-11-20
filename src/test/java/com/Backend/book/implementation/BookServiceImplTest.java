package com.Backend.book.implementation;

import com.Backend.book.dto.*;
import com.Backend.book.enums.OrderBy;
import com.Backend.book.exceptions.*;
import com.Backend.book.model.Book;
import com.Backend.book.model.Cover;
import com.Backend.book.model.Genres;
import com.Backend.book.repository.BookRepository;
import com.Backend.book.service.BookService;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static com.Backend.book.enums.OrderBy.*;
import static com.Backend.utility.ELibraryUtility.*;
import static com.Backend.utility.ELibraryUtility.DELETED_SUCCESSFULLY;
import static com.Backend.utility.ELibraryUtility.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.*;
import static org.springframework.http.HttpStatus.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookServiceImplTest {
    private BookService underTest;
    @Mock
    private BookRepository repository;
    private static final String ean = barcodeGeneration(),
            isbn = barcodeGeneration(),
            invalidIsbn = invalidBarcodeGeneration(),
            invalidEan = invalidBarcodeGeneration(),
            shortIsbn = "230",
            shortEan = "230";
    private static final Long id = 1L;
    private static final short pages = 400,
            quantity = 2,
            invalidPages = -400,
            invalidQuantity = -1;
    private static final Book book = new Book(null, "title", "author", 1994,
            "p", new BigDecimal("94.99"), new ArrayList<>(), Cover.SOFT, ean, isbn, pages, quantity, true, LocalDate.now());


    private static final String BOOK_WITH_ISBN = "Book with ISBN: ";
    private static final String BOOK_WITH_EAN = "Book with EAN: ";
    private static final String BOOK_WITH_ID = "Book with id: ";
    private static final String IS_TOO_SHORT = " is too short";
    private static final String NOT_ONLY_DIGITS = " does not contain only digits";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        underTest = new BookServiceImpl(repository);
    }

    /*
     * Testing adding a book method
     * */
    @Test
    @Order(1)
    @DisplayName("Adds book successfully")
    public void itShould_SaveNewBook() {
        var request = addBookRequest(ean, isbn, pages, quantity);
        AddBookResponse response = underTest.addBook(request);
        assertEquals(200, response.status());
        assertEquals("Book " + CREATED_SUCCESSFULLY, response.message());
        verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("When adding a book throws BookIsbnException when isbn is short")
    public void itShould_ThrowBookIsbnException_When_AddingABook_When_IsbnIsToShort() {
        var request = addBookRequest(ean, shortIsbn, pages, quantity);

        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookIsbnException.class)
                .hasMessageContaining(BOOK_WITH_ISBN + shortIsbn + IS_TOO_SHORT);
    }

    @Test
    @DisplayName("When adding a book throws BookIsbnException when isbn is not only digits")
    public void itShouldThrowBookIsbnExceptionWhenAddingABook_When_IsbnIsNotOnlyDigits() {
        var request = addBookRequest(ean, invalidIsbn, pages, quantity);

        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookIsbnException.class)
                .hasMessageContaining(BOOK_WITH_ISBN + invalidIsbn + NOT_ONLY_DIGITS);
    }

    @Test
    @DisplayName("When adding a book throws BookEanException when ean is short")
    public void itShouldThrowBookEanExceptionWhenAddingABook_When_EanIsToShort() {
        var request = addBookRequest(shortEan, isbn, pages, quantity);


        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookEanException.class)
                .hasMessageContaining(BOOK_WITH_EAN + shortEan + IS_TOO_SHORT);
    }

    @Test
    @DisplayName("When adding a book throws BookEanException when ean is not only digits")
    public void itShouldThrowBookEanExceptionWhen_AddingABook_When_EanIsNotOnlyDigits() {
        var request = addBookRequest(invalidEan, isbn, pages, quantity);


        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookEanException.class)
                .hasMessageContaining(BOOK_WITH_EAN + invalidEan + NOT_ONLY_DIGITS);
    }

    @Test
    @DisplayName("When adding a book throws BookPagesException when pages are below 0")
    public void itShould_ThrowBookPagesException_When_AddingABook_When_PagesAreBelowZero() {
        AddBookRequest request = addBookRequest(ean, isbn, invalidPages, quantity);
        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookPagesException.class)
                .hasMessageContaining("Book's pages are below 0");
    }

    @Test
    @DisplayName("When adding a book throws BookQuantityException when quantity is below 0")
    public void itShould_ThrowBookQuantityException_When_AddingABook_When_QuantityIsBelowZero() {
        AddBookRequest request = addBookRequest(ean, isbn, pages, invalidQuantity);
        assertThatThrownBy(() -> underTest.addBook(request))
                .isInstanceOf(BookQuantityException.class)
                .hasMessageContaining("Book's quantity is below 0");
    }
    /*
     * Testing delete method
     * */

    @Test
    @DisplayName("Deletes book successfully")
    public void itShould_DeleteBook() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(true);
        doNothing().when(repository).deleteById(id);

        BookDeleteResponse response = underTest.deleteBook(id);

        assertEquals(200, response.status());
        assertEquals(BOOK_WITH_ID + id + " " + DELETED_SUCCESSFULLY, response.message());

    }

    @Test
    @DisplayName("Throws BookNotFound exception when book does not exist with this id")
    public void itShould_ThrowBookNotFound_When_TryingToDelete_When_IdIsWrong() {
        Long id = 1L;
        when(repository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.deleteBook(id))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(BOOK_WITH_ID + id + " " + NOT_FOUND);
    }

    /*
     * Testing update a book method
     * */
    @Test
    @DisplayName("Should update a book")
    public void itShould_UpdateABook() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest updateRequest = updateBookRequest(ean, isbn, pages, quantity);

        UpdateBookResponse response = underTest.updateBook(updateRequest);
        assertEquals(200, response.status());
        assertEquals(BOOK_WITH_ID + id + " " + UPDATED_SUCCESSFULLY, response.message());

        verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("When updating a book throws BookIsbnException when isbn is short")
    public void itShould_ThrowBookIsbnException_When_UpdatingABook_When_IsbnIsToShort() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest request = updateBookRequest(ean, shortIsbn, pages, quantity);


        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookIsbnException.class)
                .hasMessageContaining(BOOK_WITH_ISBN + shortIsbn + IS_TOO_SHORT);
    }

    @Test
    @DisplayName("When updating a book throws BookIsbnException when isbn is not only digits")
    public void itShould_ThrowBookIsbnException_When_UpdatingABook_When_NotOnlyDigits() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest request = updateBookRequest(ean, invalidIsbn, pages, quantity);
        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookIsbnException.class)
                .hasMessageContaining(BOOK_WITH_ISBN + invalidIsbn + NOT_ONLY_DIGITS);
    }

    @Test
    @DisplayName("When updating a book throws BookEanException when ean is too short")
    public void itShould_ThrowBookEanException_When_UpdatingABook_When_IsToShort() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest request = updateBookRequest(shortEan, isbn, pages, quantity);

        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookEanException.class)
                .hasMessageContaining(BOOK_WITH_EAN + shortEan + IS_TOO_SHORT);
    }

    @Test
    @DisplayName("When updating a book throws BookEanException when ean is not only digits")
    public void itShould_ThrowBookEanException_When_UpdatingABook_When_NotOnlyDigits() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest request = updateBookRequest(invalidEan, isbn, pages, quantity);

        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookEanException.class)
                .hasMessageContaining(BOOK_WITH_EAN + invalidEan + NOT_ONLY_DIGITS);
    }

    @Test
    @DisplayName("When updating a book throws BookPagesException when pages are below 0")
    public void itShould_ThrowBookPagesException_When_UpdatingABook_When_AreBelowZero() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        UpdateBookRequest request = updateBookRequest(ean, isbn, invalidPages, quantity);
        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookPagesException.class)
                .hasMessageContaining("Book's pages are below 0");
    }

    @Test
    @DisplayName("When updating a book throws BookNotFound when given a wrong id")
    public void itShould_ThrowBookNotFoundException_When_UpdatingABook_When_IdIsWrong() {
        UpdateBookRequest request = updateBookRequest(ean, isbn, pages, quantity);
        when(repository.existsById(id)).thenReturn(false);
        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining(BOOK_WITH_ID + id + " " + NOT_FOUND);
    }


    @Test
    @DisplayName("When updating a book throws BookQuantityException when quantity is below 0")
    public void itShould_ThrowBookQuantityException_When_UpdatingABook() {
        when(repository.existsById(id)).thenReturn(true);
        when(repository.findById(id)).thenReturn(Optional.of(book));
        var request = updateBookRequest(ean, isbn, pages, invalidQuantity);
        assertThatThrownBy(() -> underTest.updateBook(request))
                .isInstanceOf(BookQuantityException.class)
                .hasMessageContaining("Book's quantity is below 0");
    }

    /*
     * Testing book pagination
     * */

    @Test
    @DisplayName("Should get pagination")
    public void should_Get_Pagination() {
        Integer pageSize = 24;
        List<Book> bookList = createBookList(pageSize);
        Page<Book> booksPage = new PageImpl<>(bookList, PageRequest.of(0, pageSize), bookList.size());
        when(repository.findAll(Mockito.any(PageRequest.class))).thenReturn(booksPage);

        BookPaginationResponse response = underTest.pagination(new BookPaginationRequest(pageSize, 0, null, null, null));
        assertEquals(OK.value(), response.status());
        assertEquals(SUCCESS, response.message());
        assertEquals(pageSize, response.data().size());
        assertEquals(0, response.pages());
    }

    @Test
    @DisplayName("Should throw a PaginationSizeException when  not given proper limit")
    public void itShould_Throw_PaginationSizeException() {
        assertThatThrownBy(() -> underTest.pagination(new BookPaginationRequest(22, 0, null, null, null)))
                .isInstanceOf(PaginationSizeException.class)
                .hasMessageContaining("Invalid size");
    }

    @Test
    @DisplayName("Should throw a PaginationPageException when  not given proper page")
    public void itShould_Throw_PaginationPageException() {
        assertThatThrownBy(() -> underTest.pagination(new BookPaginationRequest(12, -2, null, null, null)))
                .isInstanceOf(PaginationPageException.class)
                .hasMessageContaining("Invalid page");
    }

    @Test
    @DisplayName("Should return a desc list of books")
    public void itShould_Return_Desc_List() {
        Integer pageSize = 4;
        List<Book> bookList = createBookList(pageSize);
        Page<Book> booksPage = new PageImpl<>(bookList, PageRequest.of(0, pageSize), bookList.size());
        when(repository.findAll(Mockito.any(Pageable.class)))
                .thenReturn(booksPage);
        BookPaginationRequest request = new BookPaginationRequest(pageSize, 0, OrderBy.DESC, null, null);
        BookPaginationResponse result = underTest.pagination(request);

        assertEquals(OK.value(), result.status());
        assertEquals(SUCCESS, result.message());
        assertEquals(4, result.data().size());
    }

    @Test
    @DisplayName("Should return list found by genres")
    public void testFindByGenres() {
        Integer pageSize = 4;
        List<Book> bookList = createBookList(pageSize);
        Page<Book> booksPage = new PageImpl<>(bookList, PageRequest.of(0, pageSize), bookList.size());
        when(repository.findByGenres(Mockito.any(Pageable.class), Mockito.any(Genres.class)))
                .thenReturn(booksPage);

        BookPaginationRequest request = new BookPaginationRequest(pageSize, 0, OrderBy.ASC, null, Genres.FICTION);
        BookPaginationResponse result = underTest.pagination(request);
        assertEquals(OK.value(), result.status());
        assertEquals(SUCCESS, result.message());
        assertEquals(4, result.data().size());
    }

    @Test
    @DisplayName("Should throw OrderByException when not given proper order")
    public void itShould_Throw_OrderByException() {
        var request = new BookPaginationRequest(12, 1, TESTING, null, null);
        assertThatThrownBy(() -> underTest.pagination(request))
                .isInstanceOf(OrderByException.class)
                .hasMessageContaining("Order: " + request.order() + " is invalid");
    }

    @Test
    @DisplayName("Should throw a GenreFiltrationException when not given proper genres")
    public void itShould_Throw_GenreFiltrationException_When_Given_Wrong_Genre() {
        var request = new BookPaginationRequest(12, 0, null, null, Genres.TESTING);
        assertThatThrownBy(() -> underTest.pagination(request))
                .isInstanceOf(GenreFiltrationException.class)
                .hasMessageContaining("Genre " + request.genres() + " is invalid");
    }

    @Test
    @DisplayName("Should throw BookFieldException when not given proper field")
    public void itShould_Throw_BookFieldException_When_Given_Wrong_Field() {
        var request = new BookPaginationRequest(null, null, null, "MMs", null);
        assertThatThrownBy(() -> underTest.pagination(request))
                .isInstanceOf(BookFieldException.class)
                .hasMessageContaining("Field: " + request.field() + " is " + INVALID_INFORMATION);
    }

    /*
     *Testing book quantities
     * */

    @Test
    @DisplayName("Should update book quantity")
    public void should_UpdateBookQuantity() {
        when(repository.findById(id)).thenReturn(Optional.of(book));

        var response = underTest.updateBookQuantity(new UpdateBookQuantityRequest(id, (short) 1));
        assertEquals(OK.value(), response.status());
        verify(repository, times(1)).save(book);
    }

    @Test
    @DisplayName("Should throw BookQuantityException when given wrong id")
    public void itShould_Throw_BookQuantityException_When_Given_Wrong_Id() {
        assertThatThrownBy(() -> underTest.updateBookQuantity(new UpdateBookQuantityRequest(id, (short) 2)))
                .isInstanceOf(BookNotFoundException.class)
                .hasMessageContaining("Book with id: " + id + " not found");
    }

    @Test
    @DisplayName("Should throw BookQuantityException when given wrong quantity")
    public void itShould_Throw_BookQuantityException_When_Given_Wrong_Quantity() {
        when(repository.findById(id)).thenReturn(Optional.of(book));
        assertThatThrownBy(() -> underTest.updateBookQuantity(new UpdateBookQuantityRequest(id, (short) 100)))
                .isInstanceOf(BookQuantityException.class)
                .hasMessageContaining("Not enough quantities");
    }

    /*
     * Custom methods
     * */
    private AddBookRequest addBookRequest(String ean, String isbn, Short pages, Short quantity) {
        return new AddBookRequest("title", "author", 1994,
                "p", new BigDecimal("94.99"), new ArrayList<>(), Cover.SOFT, ean, isbn, pages, quantity, true);
    }

    private UpdateBookRequest updateBookRequest(String ean, String isbn, Short pages, Short quantity) {
        return new UpdateBookRequest(id, "title", "author", 1994,
                "p", new BigDecimal("94.99"), new ArrayList<>(), Cover.SOFT, ean, isbn, pages, quantity, true);
    }


    private List<Book> createBookList(Integer pageSize) {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i <= pageSize; i++) {
            list.add(new Book((long) i, null, null, null, null, null, null, null, null, null, null, null, null, null));
        }
        return list;
    }


    private static String barcodeGeneration() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= 12; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    private static String invalidBarcodeGeneration() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("S");
        for (int i = 1; i <= 12; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }


}