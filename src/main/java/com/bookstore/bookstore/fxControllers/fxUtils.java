package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.Main;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.kordamp.bootstrapfx.BootstrapFX;

public class fxUtils {
    public static void setStageParameters(Stage stage, Scene scene, boolean modal) {
        stage.setTitle("Bookstore!");
        // Load and set the stage icon
        Image icon = new Image(Main.class.getResourceAsStream("graphics/Book-icon.png"));
        stage.getIcons().add(icon);
        stage.setScene(scene);
        //Boostrap
        scene.getStylesheets().add(BootstrapFX.bootstrapFXStylesheet());
        //Load custom styles
        String customStylesheet = Main.class.getResource("styles/styles.css").toExternalForm();
        scene.getStylesheets().add(customStylesheet);
        Pane pane = (Pane) scene.getRoot();
        pane.getStyleClass().add("root-pane-style");
        if (modal) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        } else {
            stage.show();
        }
    }

    public static void generateAlert(Alert.AlertType alertType, String header, String text){
        //See here, you can just copy paste - https://code.makery.ch/blog/javafx-dialogs-official/
        Alert alert = new Alert(alertType);
        alert.setTitle("System message");
        alert.setHeaderText(header);
        alert.setContentText(text);
        alert.showAndWait();
    }
}
