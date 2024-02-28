package com.Backend.book.utility;

import com.Backend.book.dto.AddBookRequest;
import com.Backend.book.dto.UpdateBookRequest;
import com.Backend.book.model.Book;
import com.Backend.book.model.Cover;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public interface BookTestUtility {
    Short pages = 400;
    Short quantity = 22;
    String shortIsbn = "321";
    String shortEan = "231";
    String ean = barcodeGeneration();
    String isbn = barcodeGeneration();
    String invalidIsbn = invalidBarcodeGeneration();
    String invalidEan = invalidBarcodeGeneration();
    Short invalidPages = -4;
    Short invalidQuantity = -1;

    /*
     * Custom methods
     * */
    static AddBookRequest addBookRequest(String ean, String isbn, Short pages, Short quantity) {
        return new AddBookRequest("title", "author", 1994,
                "p", new BigDecimal("94.99"), new ArrayList<>(), Cover.SOFT, ean, isbn, pages, quantity, true);
    }

    static UpdateBookRequest updateBookRequest(String ean, String isbn, Short pages, Short quantity) {
        return new UpdateBookRequest(1L, "title", "author", 1994,
                "p", new BigDecimal("94.99"), new ArrayList<>(), Cover.SOFT, ean, isbn, pages, quantity, true);
    }


    static List<Book> createBookList(Integer pageSize) {
        List<Book> list = new ArrayList<>();
        for (int i = 1; i <= pageSize; i++) {
            list.add(new Book((long) i, null, null, null, null, null, null, null, null, null, null, null, null, null));
        }
        return list;
    }


    static String barcodeGeneration() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i <= 12; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    static String invalidBarcodeGeneration() {
        Random random = new Random();
        StringBuilder builder = new StringBuilder("S");
        for (int i = 1; i <= 12; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

}
