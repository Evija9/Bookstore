package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.Main;
import com.bookstore.bookstore.hibernateControlers.GenericHibernate;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.Customer;
import com.bookstore.bookstore.model.Manager;
import com.bookstore.bookstore.model.User;
import jakarta.persistence.EntityManagerFactory;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Registration {

    @FXML
    public TextField login;
    @FXML
    public PasswordField pswd;
    @FXML
    public PasswordField repeatPswd;
    @FXML
    public TextField name;
    @FXML
    public TextField surname;
    @FXML
    public DatePicker birthDate;
    @FXML
    public RadioButton customer;
    @FXML
    public RadioButton manager;
    @FXML
    public TextField deliveryAddress;
    @FXML
    public TextField billAddress;
    @FXML
    public TextField cardNo;
    @FXML
    public TextField uniqueID;
    @FXML
    public TextField mcID;
    @FXML
    public CheckBox isAdmin;
    @FXML
    public DatePicker employmentDate;
    @FXML
    public Button returnToLogin;

    private EntityManagerFactory entityManagerFactory;

    public void returnToLogin() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-form.fxml"));
        Parent parent = fxmlLoader.load();
        Stage stage = (Stage) login.getScene().getWindow();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, false);
    }

    private void toggleReturnToLoginButton(boolean showButton) {
        // Show or hide the button based on the parameter
        returnToLogin.setVisible(showButton);
    }

    public void createUser() {
        // Check if all required fields are filled
        if (areAllFieldsFilled()) {
            HibernateShop hibernateShop = new HibernateShop(entityManagerFactory);
            String userLogin = login.getText();
            // Check if user with the same login already exists
            if (userWithLoginExists(hibernateShop, userLogin)) {
                // Show an alert if user with the same login already exists
                fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration Error", "User with the same login already exists");
            } else {
                // Proceed with user creation
                if (customer.isSelected()) {
                    User user = new Customer(name.getText(), surname.getText(), userLogin, pswd.getText(), cardNo.getText(), deliveryAddress.getText(), billAddress.getText(), birthDate.getValue());
                    hibernateShop.create(user);
                } else {
                    User user = new Manager(name.getText(), surname.getText(), userLogin, pswd.getText(), uniqueID.getText(), mcID.getText(), isAdmin.isSelected(), employmentDate.getValue());
                    hibernateShop.create(user);
                }
                // Show a registration success message
                fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration Success", "User registered successfully");
            }
        } else {
            // Show an alert if not all required fields are filled
            fxUtils.generateAlert(Alert.AlertType.INFORMATION, "Registration Error", "All fields were not filled");
        }
    }

    private boolean areAllFieldsFilled() {
        // Check if all required fields are filled based on the selected user type
        if (customer.isSelected()) {
            return !name.getText().isEmpty() && !surname.getText().isEmpty() && !login.getText().isEmpty() && !pswd.getText().isEmpty() &&
                    !cardNo.getText().isEmpty() && !deliveryAddress.getText().isEmpty() && !billAddress.getText().isEmpty() && birthDate.getValue() != null;
        } else {
            return !name.getText().isEmpty() && !surname.getText().isEmpty() && !login.getText().isEmpty() && !pswd.getText().isEmpty() &&
                    !uniqueID.getText().isEmpty() && !mcID.getText().isEmpty() && employmentDate.getValue() != null;
        }
    }

    private boolean userWithLoginExists(HibernateShop hibernateShop, String login) {
        // Iterate through the users and check if any user has the same login
        List<User> users = hibernateShop.getAllRecords(User.class);
        for (User user : users) {
            if (user.getLogin().equals(login)) {
                return true; // User with the same login already exists
            }
        }
        return false; // User with the same login does not exist
    }

    public void setData(EntityManagerFactory entityManagerFactory, boolean showManagerFields, boolean showReturnButton) {
        this.entityManagerFactory = entityManagerFactory;
        disableFields(showManagerFields);
        toggleReturnToLoginButton(showReturnButton);
    }

    public void setData(EntityManagerFactory entityManagerFactory, Manager manager) {
        this.entityManagerFactory = entityManagerFactory;
        toggleFields(customer.isSelected(), manager);
    }

    private void disableFields(boolean showManagerFields) {
        if (!showManagerFields) {
            uniqueID.setVisible(false);
            mcID.setVisible(false);
            employmentDate.setVisible(false);
            isAdmin.setVisible(false);
            manager.setVisible(false);
        } else {
            uniqueID.setVisible(true);
            mcID.setVisible(true);
            employmentDate.setVisible(true);
            isAdmin.setVisible(true);
            manager.setVisible(true);
            deliveryAddress.setVisible(false);
            cardNo.setVisible(false);
            billAddress.setVisible(false);
        }
    }

    private void toggleFields(boolean isManager, Manager manager) {
        if (isManager) {
            deliveryAddress.setDisable(true);
            cardNo.setDisable(true);
            uniqueID.setDisable(false);
            mcID.setDisable(false);
            employmentDate.setDisable(false);
            if (manager.isAdmin()) isAdmin.setDisable(false);
        } else {
            deliveryAddress.setDisable(false);
            cardNo.setDisable(false);
            uniqueID.setDisable(true);
            mcID.setDisable(true);
            employmentDate.setDisable(true);
            isAdmin.setDisable(true);
        }
    }

}
