package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.Main;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProductReview {
    @FXML
    public ListView<Product> productList;
    @FXML
    public TreeView<Comment> commentTree;

    private EntityManagerFactory entityManagerFactory;
    private HibernateShop hibernateShop;
    private User user;

    public void previewProduct() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Product product = hibernateShop.getEntityById(Product.class, productList.getSelectionModel().getSelectedItem().getId());
        if (product instanceof Book book) {
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, book.getTitle(), book.toString());
            alert.setHeaderText(book.getTitle());
            alert.setContentText(book.toString());
        } else if (product instanceof Accessories acc) {
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, acc.getTitle(), acc.toString());
            alert.setHeaderText(acc.getTitle());
            alert.setContentText(acc.toString());
        } else if (product instanceof Stationery stat){
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, stat.getTitle(), stat.toString());
            alert.setHeaderText(stat.getTitle());
            alert.setContentText(stat.toString());
        }
        alert.showAndWait();
    }

    public void addReview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-form.fxml"));
        Parent parent = fxmlLoader.load();

        CommentForm commentForm = fxmlLoader.getController();
        commentForm.setData(entityManagerFactory, user, productList.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    public void loadReviews() {
        Product product = hibernateShop.getEntityById(Product.class, productList.getSelectionModel().getSelectedItem().getId());
        commentTree.setRoot(new TreeItem<>());
        commentTree.setShowRoot(false);
        commentTree.getRoot().setExpanded(true);
        product.getComments().forEach(comment -> addTreeItem(comment, commentTree.getRoot()));
    }

    public void updateReview() throws IOException {
        Comment selectedComment = commentTree.getSelectionModel().getSelectedItem().getValue();
        //if (selectedComment.getCommentOwner().equals(user)) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-form.fxml"));
            Parent parent = fxmlLoader.load();

            CommentForm commentForm = fxmlLoader.getController();
            commentForm.setData(entityManagerFactory, user, selectedComment); // Pass user as well
            Stage stage = new Stage();
            Scene scene = new Scene(parent);
            fxUtils.setStageParameters(stage, scene, true);
        //} else {
            //fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Comment owner error", "This comment was created by another user.");
        //}
    }

    public void deleteReview() {
        TreeItem<Comment> selectedCommentItem = commentTree.getSelectionModel().getSelectedItem();
        Comment selectedComment = selectedCommentItem.getValue();
        hibernateShop.deleteComment(selectedComment.getId());
        // Remove the selected comment from the tree view
        selectedCommentItem.getParent().getChildren().remove(selectedCommentItem);
    }

    public void reply() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("comment-form.fxml"));
        Parent parent = fxmlLoader.load();
        CommentForm commentForm = fxmlLoader.getController();
        commentForm.setData(entityManagerFactory, commentTree.getSelectionModel().getSelectedItem().getValue(), user);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.hibernateShop = new HibernateShop(entityManagerFactory);
        this.user = user;
        fillLists();
    }

    private void fillLists() {
        productList.getItems().clear();
        productList.getItems().addAll(hibernateShop.getAllRecords(Product.class));
    }

}
