package com.bookstore.bookstore.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public final class Accessories extends Product {
    private String Category;
    private String Material;
    private String Colour;

    public Accessories(String title, String description, int qty, float weight, String Category, String Material, String Colour){
        super(title, description, qty, weight);
        this.Category = Category;
        this.Material = Material;
        this.Colour = Colour;
    }

    public Accessories(String title, String description, int qty, float weight, String category) {
        super(title, description, qty, weight);
        Category = category;
    }


    public Accessories(String title, String description, int qty, float weight) {
        super(title, description, qty, weight);
    }


    @Override
    public String toString() {
        return title + ":" + qty;
    }
}
