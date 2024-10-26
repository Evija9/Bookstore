package com.bookstore.bookstore.model;

import com.bookstore.bookstore.model.Cart;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public final class Manager extends User {
    private LocalDate emplDate;
    private boolean isAdmin;

    @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Cart> myManagedCarts = new ArrayList<>();
    @ManyToOne
    private Warehouse warehouse;

    public Manager(String name, String surname, String login, String password, boolean isAdmin) {
        super(name, surname, login, password);
        this.isAdmin = isAdmin;
    }

    public Manager(String name, String surname, String login, String password, String uniqueID, String mcID, boolean isAdmin, LocalDate emplDate) {
        super(name, surname, login, password);
        this.isAdmin = isAdmin;
        this.emplDate = emplDate;
    }

    @Override
    public String toString() {
        return "Manager{" +
                "isAdmin=" + isAdmin +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
