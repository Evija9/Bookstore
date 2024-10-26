package com.bookstore.bookstore;

import com.bookstore.bookstore.fxControllers.fxUtils;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-form.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        fxUtils.setStageParameters(stage, scene, false);
    }

    public static void main(String[] args) {
        launch();
    }
}
