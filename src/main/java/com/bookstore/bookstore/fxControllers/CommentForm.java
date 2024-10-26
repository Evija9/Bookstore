package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.hibernateControlers.GenericHibernate;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import lombok.Setter;

public class CommentForm {
    @FXML
    public Slider productRating;
    @FXML
    public TextField commentTitle;
    @FXML
    public TextArea commentBody;

    private EntityManagerFactory entityManagerFactory;

    private User user;
    private Product product;
    private GenericHibernate genericHibernate;
    private HibernateShop hibernateShop;
    private Comment comment;
    private boolean isUpdate;

    public void saveComment() {
        if (!isUpdate && comment == null) {
            Product currentProduct = genericHibernate.getEntityById(Product.class, product.getId());
            Comment reviewForProduct = new Comment(commentTitle.getText(), commentBody.getText(), productRating.getValue(), user, currentProduct);
            currentProduct.getComments().add(reviewForProduct);
            genericHibernate.update(currentProduct);

        } else if (!isUpdate) {
            Comment parentComment = genericHibernate.getEntityById(Comment.class, comment.getId());
            Comment replyToComment = new Comment(commentTitle.getText(), commentBody.getText(), user, parentComment);
            parentComment.getReplies().add(replyToComment);
            genericHibernate.update(parentComment);
        } else {
            Comment commentToUpdate = genericHibernate.getEntityById(Comment.class, comment.getId());
            commentToUpdate.setCommentBody(commentBody.getText());
            commentToUpdate.setCommentTitle(commentTitle.getText());
            genericHibernate.update(commentToUpdate);
        }
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user, Product product) {
        this.entityManagerFactory = entityManagerFactory;
        this.user = user;
        this.product = product;
        this.hibernateShop = new HibernateShop(entityManagerFactory);
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Comment comment, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.comment = comment;
        this.user = user;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
    }


    public void setData(EntityManagerFactory entityManagerFactory, User user, Comment comment) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.user = user; // Set the user first
        this.comment = comment;
        this.isUpdate = true;

        // Now retrieve the comment to update
        Comment commentToUpdate = genericHibernate.getEntityById(Comment.class, comment.getId());

        // Proceed with other initialization
        productRating.setVisible(false);
        commentTitle.setText(commentToUpdate.getCommentTitle());
        commentBody.setText(commentToUpdate.getCommentBody());
    }
}
