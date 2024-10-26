package com.bookstore.bookstore.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String CommentTitle;
    private String CommentBody;
    private LocalDate dateCreated;
    @ManyToOne
    private User commentOwner;
    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Comment> replies;
    @ManyToOne
    private Comment parentComment;
    @ManyToOne
    private Product parentProduct;
    private float rating;
    @ManyToOne
    private Cart chatComment;

    public Comment(String CommentTitle, String CommentBody, double rating, User commentOwner, Product parentProduct) {
        this.CommentTitle = CommentTitle;
        this.CommentBody = CommentBody;
        this.rating = (float) rating;
        this.commentOwner = commentOwner;
        this.parentProduct = parentProduct;
    }

    public Comment(String CommentTitle, String CommentBody, User commentOwner, Comment parentComment) {
        this.CommentTitle = CommentTitle;
        this.CommentBody = CommentBody;
        this.commentOwner = commentOwner;
        this.parentComment = parentComment;
    }

    public Comment(String CommentTitle, String CommentBody, User commentOwner, Cart chatComment) {
        this.CommentTitle = CommentTitle;
        this.CommentBody = CommentBody;
        this.commentOwner = commentOwner;
        this.chatComment = chatComment;
    }


    @Override
    public String toString() { return CommentTitle + ":" +
            " " + CommentBody;}
}
