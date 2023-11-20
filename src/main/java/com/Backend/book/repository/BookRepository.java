package com.Backend.book.repository;

import com.Backend.book.model.Book;
import com.Backend.book.model.Genres;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;


public interface BookRepository extends PagingAndSortingRepository<Book, Long>, JpaRepository<Book, Long> {

    Page<Book> findByGenres(Pageable pageable, Genres genres);
}
