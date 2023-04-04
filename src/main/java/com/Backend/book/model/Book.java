package com.Backend.book.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
@Builder
@Table(name = "book")
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private Integer publishedOn;
    private String publisher;
    private BigDecimal price;
    private List<String> genres;
}
