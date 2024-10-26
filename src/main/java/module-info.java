module com.bookstore.bookstore {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires lombok;
    requires mysql.connector.j;
    requires java.sql;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jakarta.persistence;

    opens com.bookstore.bookstore to javafx.fxml;
    exports com.bookstore.bookstore;
    opens com.bookstore.bookstore.fxControllers to javafx.fxml;
    exports com.bookstore.bookstore.fxControllers to javafx.fxml;
    opens com.bookstore.bookstore.fxControllers.tableParameters to javafx.fxml, javafx.base;
    exports com.bookstore.bookstore.fxControllers.tableParameters to javafx.fxml, java.base;
    opens com.bookstore.bookstore.model to javafx.fxml,org.hibernate.orm.core, jakarta.persistence;
    exports com.bookstore.bookstore.model to javafx.fxml,org.hibernate.orm.core, jakarta.persistence;
}