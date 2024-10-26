package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.Main;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.User;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginForm  implements Initializable {
    @FXML
    public TextField loginField;
    @FXML
    public PasswordField pswdField;

    private EntityManagerFactory entityManagerFactory;

    public void validateAndLogIn() throws IOException {
        HibernateShop hibernateShop = new HibernateShop(entityManagerFactory);
        var user = hibernateShop.getUserByCredentials(loginField.getText(), pswdField.getText());
        //If user exists, open main window form
        if (user != null) {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main window.fxml"));
            Parent parent = fxmlLoader.load();

            MainWindow mainWindow = fxmlLoader.getController();
            mainWindow.setData(entityManagerFactory, (User) user);
            Scene scene = new Scene(parent);
            Stage stage = (Stage) loginField.getScene().getWindow();
            stage.setScene(scene);
            fxUtils.setStageParameters(stage, scene, false);
        } else {
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Login error", "No such user or wrong credentials");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        entityManagerFactory = Persistence.createEntityManagerFactory("ShopSystem");
    }

    public void openRegistrationForm() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();

        //Access controller of main window. Each form has its own controller, so make sure that you make no mistake here
        Registration registration = fxmlLoader.getController();
        registration.setData(entityManagerFactory, false, true);
        Scene scene = new Scene(parent);
        Stage stage = (Stage) loginField.getScene().getWindow();
        fxUtils.setStageParameters(stage, scene, false);
    }
}
