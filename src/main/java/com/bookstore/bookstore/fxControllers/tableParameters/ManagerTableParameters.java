package com.bookstore.bookstore.fxControllers.tableParameters;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;

public class ManagerTableParameters extends UserTableParameters {
    SimpleStringProperty emplDate = new SimpleStringProperty();
    SimpleBooleanProperty isAdmin = new SimpleBooleanProperty();

    public ManagerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password, SimpleStringProperty emplDate ,SimpleBooleanProperty isAdmin) {
        super(id, login, name, surname, password);
        this.emplDate = emplDate;
        this.isAdmin = isAdmin;
    }

    public ManagerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password) {
        super(id, login, name, surname, password);
    }

    public ManagerTableParameters() {

    }


    public boolean isIsAdmin() {
        return isAdmin.get();
    }

    public SimpleBooleanProperty isAdminProperty() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin.set(isAdmin);
    }

    public String getEmplDate() {
        return emplDate.get();
    }

    public SimpleStringProperty emplDateProperty() {
        return emplDate;
    }

    public void setEmplDate(String emplDate) {
        this.emplDate.set(emplDate);
    }
}
