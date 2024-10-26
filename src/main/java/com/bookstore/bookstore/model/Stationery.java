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
public class Stationery extends Product {
    private String Brand;
    private String Type;

    public Stationery(String title, String description, int qty, float weight, String Brand, String Type){
        super(title, description, qty, weight);
        this.Brand = Brand;
        this.Type = Type;

    }

    public Stationery(String title, String description, int qty, float weight, String brand) {
        super(title, description, qty, weight);
        Brand = brand;
    }

    public Stationery(String title, String description, int qty, float weight) {
        super(title, description, qty, weight);
    }


    @Override
    public String toString() {
        return title + ":" + qty;
    }
}
