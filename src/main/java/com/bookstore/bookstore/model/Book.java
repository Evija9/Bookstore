package com.bookstore.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity

public final class Book extends Product {
    private String Author;
    private LocalDate publicDate;
    @Enumerated
    private Genre genre;

    public Book(String title, String description, int qty, float weight, String Author, LocalDate publicDate, Genre genre){
        super(title, description, qty, weight);
        this.Author = Author;
        this.publicDate = publicDate;
        this.genre = genre;
    }

    public Book(String label, String description, int qty, float weight, String Author) {
        super(label, description, qty, weight);
        Author = Author;
    }

    public Book(String label, String description, int qty, float weight, LocalDate publicDate) {
        super(label, description, qty, weight);
        this.publicDate = publicDate;
    }


    public Book(String label, String description, int qty, float weight) {
        super(label, description, qty, weight);
    }

    public Book(int id, String title, String description, int qty, float weight, LocalDate publicDate) {
    }


    @Override
    public String toString() {
        return title + ":" + qty;
    }
}
