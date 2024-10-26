package com.bookstore.bookstore.fxControllers.tableParameters;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;

public class CustomerTableParameters extends UserTableParameters {
    SimpleStringProperty cardNo = new SimpleStringProperty();
    SimpleStringProperty deliveryAddress = new SimpleStringProperty();
    SimpleStringProperty billingAddress = new SimpleStringProperty();
    SimpleStringProperty birthDate = new SimpleStringProperty();

   public CustomerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password, SimpleStringProperty cardNo, SimpleStringProperty deliveryAddress, SimpleStringProperty billingAddress, SimpleStringProperty birthDate) {
        super(id, login, name, surname, password);
        this.cardNo = cardNo;
        this.deliveryAddress = deliveryAddress;
        this.billingAddress = billingAddress;
        this.birthDate = birthDate;
    }

    public CustomerTableParameters(SimpleIntegerProperty id, SimpleStringProperty login, SimpleStringProperty name, SimpleStringProperty surname, SimpleStringProperty password) {
        super(id, login, name, surname, password);
    }

    public CustomerTableParameters() {

    }


    public String getCardNo() {
        return cardNo.get();
    }

    public SimpleStringProperty cardNoProperty() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo.set(cardNo);
    }

    public String getDeliveryAddress() {
        return deliveryAddress.get();
    }

    public SimpleStringProperty deliveryAddressProperty() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress.set(deliveryAddress);
    }

    public String getBillingAddress() {
        return billingAddress.get();
    }

    public SimpleStringProperty billingAddressProperty() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress.set(billingAddress);
    }

    public String getBirthDate() {
        return birthDate.get();
    }

    public SimpleStringProperty birthDateProperty() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate.set(birthDate);
    }
}
