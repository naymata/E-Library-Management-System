package com.Backend.book.repository;

import com.Backend.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BookRepository extends JpaRepository<Book,Long> {

}
