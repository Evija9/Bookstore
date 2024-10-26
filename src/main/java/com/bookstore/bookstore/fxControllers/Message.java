package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.hibernateControlers.GenericHibernate;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.Cart;
import com.bookstore.bookstore.model.Comment;
import com.bookstore.bookstore.model.Product;
import com.bookstore.bookstore.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Message {
    @FXML
    public TextArea messageBody;
    @FXML
    public TextField messageTitle;

    private Cart cart;

    private GenericHibernate genericHibernate;
    private User user;
    private Comment comment;
    private boolean isUpdate;
    private EntityManagerFactory entityManagerFactory;
    private HibernateShop hibernateShop;

    public void sendMessage() {
        if (!isUpdate && comment == null) {
            Cart currentCart = genericHibernate.getEntityById(Cart.class, cart.getId());
            Comment newMessage = new Comment(messageTitle.getText(), messageBody.getText(), user, currentCart);
            currentCart.getChat().add(newMessage);
            genericHibernate.update(newMessage);

        } else if (!isUpdate) {
            Comment parentComment = genericHibernate.getEntityById(Comment.class, comment.getId());
            Comment replyToComment = new Comment(messageTitle.getText(), messageBody.getText(), user, parentComment);
            parentComment.getReplies().add(replyToComment);
            genericHibernate.update(parentComment);
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user, Cart cart) {
        this.entityManagerFactory = entityManagerFactory;
        this.user = user;
        this.cart = cart;
        this.hibernateShop = new HibernateShop(entityManagerFactory);
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Comment comment, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.comment = comment;
        this.user = user;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

}
