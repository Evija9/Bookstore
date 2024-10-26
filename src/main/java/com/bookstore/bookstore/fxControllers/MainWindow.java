package com.bookstore.bookstore.fxControllers;

import com.bookstore.bookstore.Main;
import com.bookstore.bookstore.fxControllers.tableParameters.CustomerTableParameters;
import com.bookstore.bookstore.fxControllers.tableParameters.ManagerTableParameters;
import com.bookstore.bookstore.hibernateControlers.GenericHibernate;
import com.bookstore.bookstore.hibernateControlers.HibernateShop;
import com.bookstore.bookstore.model.*;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainWindow implements Initializable {
    @FXML
    public ListView<Product> productAdminList;
    @FXML
    public RadioButton bookRadio;
    @FXML
    public RadioButton accessoriesRadio;
    @FXML
    public RadioButton stationeryRadio;
    @FXML
    public TextArea productDescription;
    @FXML
    public TextField productQty;
    @FXML
    public TextField productWeight;
    @FXML
    public DatePicker bookPublicDate;
    @FXML
    public TextField bookAuthor;
    @FXML
    public TextField accMaterial;
    @FXML
    public TextField accColour;
    @FXML
    public TextField statBrand;
    @FXML
    public Button addProduct;
    @FXML
    public Button updateProduct;
    @FXML
    public Button deleteProduct;
    @FXML
    public Tab shopTab;
    @FXML
    public Tab productTab;
    @FXML
    public TextField productTitle;
    @FXML
    public TextField accCategory;
    @FXML
    public TextField statType;
    @FXML
    public ComboBox<Genre> bookGenre;
    @FXML
    public ListView<Product> shopProducts;
    @FXML
    public ListView<Product> viewProducts;
    @FXML
    public ToggleGroup productType;
    public Button leaveReview;

    public Tab usersTab;
    public TableView<ManagerTableParameters> managerTable;
    public TableColumn<ManagerTableParameters, Integer> managerID;
    public TableColumn<ManagerTableParameters, String> managerLogin;
    public TableColumn<ManagerTableParameters, String> managerName;
    public TableColumn<ManagerTableParameters, String> managerSurname;
    public TableColumn<ManagerTableParameters, String> managerPassword;
    public TableColumn<ManagerTableParameters, Boolean> managerAdmin;
    public TableColumn<ManagerTableParameters, String> managerDate;
    public TableColumn<ManagerTableParameters, Void> dummyColManager;
    public TableView<CustomerTableParameters> customerTable;
    public TableColumn<CustomerTableParameters, Integer> custID;
    public TableColumn<CustomerTableParameters, String> custLogin;
    public TableColumn<CustomerTableParameters, String> custName;
    public TableColumn<CustomerTableParameters, String> custSurname;
    public TableColumn<CustomerTableParameters, String> custCard;
    public TableColumn<CustomerTableParameters, String> custPass;
    public TableColumn<CustomerTableParameters, String> customerBirth;
    public TableColumn<CustomerTableParameters, String> custDelAddr;
    public TableColumn<CustomerTableParameters, String> custBillAddr;
    public TableColumn<CustomerTableParameters, Void> dummyColCustomer;
    @FXML
    public Tab warehousesTab;
    @FXML
    public ListView<Cart> ordersList;
    @FXML
    public Tab ordersTab;
    @FXML
    public TextField nrOfItemsFilter;
    @FXML
    public Button filterOrdersButton;
    @FXML
    public DatePicker dateOrderStart;
    @FXML
    public DatePicker dateOrderFinish;
    @FXML
    public TextField filterManagerId;
    @FXML
    public Button deleteOrdersButton;

    @FXML
    public ComboBox<City> warehouseCity;
    @FXML
    public TextField warehouseAddress;
    @FXML
    public ListView<Warehouse> warehouseList;
    @FXML
    public TreeView<Product> warehouseProducts;
    @FXML
    public TreeView<Comment> orderChat;



    private ObservableList<ManagerTableParameters> dataM = FXCollections.observableArrayList();
    private ObservableList<CustomerTableParameters> dataC = FXCollections.observableArrayList();

    @FXML
    public TabPane tabPane;

    private User user;
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("ShopSystem");
    private HibernateShop hibernateShop = new HibernateShop(entityManagerFactory);
    private GenericHibernate genericHibernate = new GenericHibernate(entityManagerFactory);

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        bookGenre.getItems().addAll(Genre.values());
        warehouseCity.getItems().addAll(City.values());

        //Manager managerT = new Manager("Vardas", "Pavarde", "Manager", "Pass", "ID", "MCID", true, LocalDate.now());
        //genericHibernate.create(managerT);
        /*Customer customerT = new Customer("Vardas", "Pavarde", "Customer", "Pass", "CardNo", "DelAddress", "BillAddress", LocalDate.now());
        genericHibernate.create(customerT);*/

        managerTable.setEditable(true);
        customerTable.setEditable(true);
        //ordersList.setEditable(true);

        //Manager Table
        managerID.setCellValueFactory(new PropertyValueFactory<>("id"));
        managerLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        managerName.setCellValueFactory(new PropertyValueFactory<>("name"));
        managerSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        managerPassword.setCellValueFactory(new PropertyValueFactory<>("password"));
        managerDate.setCellValueFactory(new PropertyValueFactory<>("emplDate"));
        managerAdmin.setCellValueFactory(new PropertyValueFactory<>("isAdmin"));

        managerName.setCellFactory(TextFieldTableCell.forTableColumn());
        managerName.setOnEditCommit(event -> {
            //event - click on cell
            //event.getNewValue - when we click on cell and enter new value
            //event knows which table was selected, which row was selected and which cell was changed
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            //Before updating, get the latest version from database
            Manager manager = hibernateShop.getEntityById(Manager.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            manager.setName(event.getNewValue());
            hibernateShop.update(manager);
        });
        Callback<TableColumn<ManagerTableParameters, Void>, TableCell<ManagerTableParameters, Void>> callbackM = param -> {
            final TableCell<ManagerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        ManagerTableParameters row = getTableView().getItems().get(getIndex());
                        hibernateShop.delete(Manager.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        dummyColManager.setCellFactory(callbackM);

        //Customer Table

        //Customer customerT = new Customer("Vardas", "Pavarde", "Customer", "Pass" , "CardNo" ,"DelAddress", "BillAddress", LocalDate.now());
        //genericHibernate.create(customerT);

        custID.setCellValueFactory(new PropertyValueFactory<>("id"));
        custLogin.setCellValueFactory(new PropertyValueFactory<>("login"));
        custName.setCellValueFactory(new PropertyValueFactory<>("name"));
        custSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        custPass.setCellValueFactory(new PropertyValueFactory<>("password"));
        customerBirth.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
        custCard.setCellValueFactory(new PropertyValueFactory<>("cardNo"));
        custDelAddr.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));
        custBillAddr.setCellValueFactory(new PropertyValueFactory<>("billingAddress"));

        custName.setCellFactory(TextFieldTableCell.forTableColumn());
        custName.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            //Before updating, get the latest version from database
            Customer customer = hibernateShop.getEntityById(Customer.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            customer.setName(event.getNewValue());
            hibernateShop.update(customer);
        });
        Callback<TableColumn<CustomerTableParameters, Void>, TableCell<CustomerTableParameters, Void>> callbackC = param -> {
            final TableCell<CustomerTableParameters, Void> cell = new TableCell<>() {
                private final Button deleteButton = new Button("Delete");

                {
                    deleteButton.setOnAction(event -> {
                        CustomerTableParameters row = getTableView().getItems().get(getIndex());
                        hibernateShop.delete(Customer.class, row.getId());
                    });
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                    }
                }
            };
            return cell;
        };
        dummyColCustomer.setCellFactory(callbackC);
        fillManagerTable();
        fillCustomerTable();

        populateWarehouseList();
        populateOrdersList();
        filterOrdersButton.setOnAction(event -> filterOrders());
        deleteOrdersButton.setOnAction(event -> deleteOrder());
    }


    private void populateWarehouseList() {
        List<Warehouse> warehouses = hibernateShop.getAllRecords(Warehouse.class);
        warehouseList.setItems(FXCollections.observableArrayList(warehouses));
    }

    private void populateOrdersList() {
        List<Cart> cart = hibernateShop.getAllRecords(Cart.class);
        ordersList.setItems(FXCollections.observableArrayList(cart));
    }

    public void filterOrders() {
        LocalDate startDate = dateOrderStart.getValue();
        LocalDate endDate = dateOrderFinish.getValue();
        String nrOfItemsText = nrOfItemsFilter.getText();
        String managersIdText = filterManagerId.getText();
        int nrOfItems = -1; // Default value for filtering by number of items
        int managersId = -1;
        try {
            if (!nrOfItemsText.isEmpty()) {
                nrOfItems = Integer.parseInt(nrOfItemsText);
            }
            if (!managersIdText.isEmpty()) {
                managersId = Integer.parseInt(managersIdText);
            }
        } catch (NumberFormatException e) {
            return;
        }

        List<Cart> allOrders = hibernateShop.getAllRecords(Cart.class);
        ObservableList<Cart> filteredOrders = FXCollections.observableArrayList();

        // Filtering by date
        if (startDate != null && endDate != null) {
            for (Cart cartItem : allOrders) {
                LocalDate orderDate = cartItem.getDateCreated();
                if (orderDate.isEqual(startDate) || orderDate.isEqual(endDate) ||
                        (orderDate.isAfter(startDate) && orderDate.isBefore(endDate))) {
                    filteredOrders.add(cartItem);
                }
            }
        } else {
            filteredOrders.addAll(allOrders);
        }

        // Filtering by number of items
        if (nrOfItems != -1) {
            ObservableList<Cart> tempOrders = FXCollections.observableArrayList(filteredOrders);
            filteredOrders.clear();
            for (Cart cartItem : tempOrders) {
                if (cartItem.getNrOfItems() == nrOfItems) {
                    filteredOrders.add(cartItem);
                }
            }
        }

        //Filtering by managersId
        // Filtering by managerId
        if (managersId != -1) {
            ObservableList<Cart> tempOrders = FXCollections.observableArrayList(filteredOrders);
            filteredOrders.clear();
            for (Cart cartItem : tempOrders) {
                Manager manager = cartItem.getManager();
                if (manager != null && manager.getId() == managersId) {
                    filteredOrders.add(cartItem);
                }
            }
        }

        ordersList.setItems(filteredOrders);
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.user = user;
        this.hibernateShop = new HibernateShop(entityManagerFactory);
        loadTabData();
        setCustomerView();
    }


    private void setCustomerView() {
        //Customer should not have any access or knowledge about tabs that are intended for Managers/Admins
        if (user instanceof Customer) {
            //You could simply disable tabs, but it is better to not render them
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(productTab);
            tabPane.getTabs().remove(warehousesTab);
        } else if (!((Manager) user).isAdmin()) {
            tabPane.getTabs().remove(usersTab);
            tabPane.getTabs().remove(shopTab);
            deleteOrdersButton.setDisable(true);
        } else if (((Manager) user).isAdmin()) {
            deleteOrdersButton.setDisable(true);
        }
    }

    private void fillManagerTable() {
        //get all records from the database for Manager TableView
        /*Manager managerT = new Manager("Vardas", "Pavarde", "Manager", "Pass", "ID", "MCID", true, LocalDate.now(), LocalDate.now());
        genericHibernate.create(managerT);*/
        List<Manager> managers = hibernateShop.getAllRecords(Manager.class);
        for (Manager m : managers) {
            ManagerTableParameters managerTableParameters = new ManagerTableParameters();
            managerTableParameters.setId(m.getId());
            managerTableParameters.setLogin(m.getLogin());
            managerTableParameters.setName(m.getName());
            managerTableParameters.setSurname(m.getSurname());
            managerTableParameters.setPassword(m.getPassword());
            managerTableParameters.setEmplDate(String.valueOf(m.getEmplDate()));
            managerTableParameters.setIsAdmin(m.isAdmin());

            dataM.add(managerTableParameters);
        }
        managerTable.setItems(dataM);
    }

    private void fillCustomerTable() {
        dataC.clear();
        /*Customer customerT = new Customer("Customer", "Vardas", "Pavarde", "Pass", "CardNo", "DelAddress", "BillAddress");
        hibernateShop.create(customerT);*/
        List<Customer> customers = hibernateShop.getAllRecords(Customer.class);
        for (Customer c : customers) {
            CustomerTableParameters customerTableParameters = new CustomerTableParameters();
            customerTableParameters.setId(c.getId());
            customerTableParameters.setLogin(c.getLogin());
            customerTableParameters.setName(c.getName());
            customerTableParameters.setSurname(c.getSurname());
            customerTableParameters.setPassword(c.getPassword());
            customerTableParameters.setCardNo(c.getCardNo());
            customerTableParameters.setDeliveryAddress(c.getDeliveryAddress());
            customerTableParameters.setBillingAddress(c.getBillingAddress());
            customerTableParameters.setBirthDate(String.valueOf(c.getBirthDate()));

            dataC.add(customerTableParameters);
        }
        customerTable.setItems(dataC);
    }


    public void createProduct() {
        // Check if all required fields are filled
        if (areAllFieldsFilled()) {
            // Retrieve a list of all available warehouses
            List<Warehouse> availableWarehouses = hibernateShop.loadAllWarehouses();
            if (bookRadio.isSelected()) {
                // Generate a random index within the range of available warehouses
                int randomIndex = (int) (Math.random() * availableWarehouses.size());
                Warehouse randomWarehouse = availableWarehouses.get(randomIndex);
                Book book = new Book(productTitle.getText(),
                        productDescription.getText(),
                        Integer.parseInt(productQty.getText()),
                        Float.parseFloat(productWeight.getText()),
                        bookAuthor.getText(),
                        bookPublicDate.getValue(),
                        bookGenre.getValue());
                // Assign the warehouse to the newly created product
                book.setWarehouse(randomWarehouse);
                hibernateShop.create(book);
                productAdminList.getItems().clear();
                productAdminList.getItems().addAll(hibernateShop.getAllRecords(Product.class));
                // Show alert for successful product addition
                showInformationAlert("Product Added", "The product was successfully added.");
            } else if (accessoriesRadio.isSelected()) {
                // Generate a random index within the range of available warehouses
                int randomIndex = (int) (Math.random() * availableWarehouses.size());
                Warehouse randomWarehouse = availableWarehouses.get(randomIndex);
                Accessories acc = new Accessories(productTitle.getText(),
                        productDescription.getText(),
                        Integer.parseInt(productQty.getText()),
                        Float.parseFloat(productWeight.getText()),
                        accCategory.getText(),
                        accMaterial.getText(),
                        accColour.getText());
                // Assign the warehouse to the newly created product
                acc.setWarehouse(randomWarehouse);
                hibernateShop.create(acc);
                productAdminList.getItems().clear();
                productAdminList.getItems().addAll(hibernateShop.getAllRecords(Product.class));
                // Show alert for successful product addition
                showInformationAlert("Product Added", "The product was successfully added.");
            } else if (stationeryRadio.isSelected()) {
                // Generate a random index within the range of available warehouses
                int randomIndex = (int) (Math.random() * availableWarehouses.size());
                Warehouse randomWarehouse = availableWarehouses.get(randomIndex);
                Stationery stat = new Stationery(productTitle.getText(),
                        productDescription.getText(),
                        Integer.parseInt(productQty.getText()),
                        Float.parseFloat(productWeight.getText()),
                        statBrand.getText(),
                        statType.getText());
                // Assign the warehouse to the newly created product
                stat.setWarehouse(randomWarehouse);
                hibernateShop.create(stat);
                productAdminList.getItems().clear();
                productAdminList.getItems().addAll(hibernateShop.getAllRecords(Product.class));
                // Show alert for successful product addition
                showInformationAlert("Product Added", "The product was successfully added.");
            }
        } else {
            // Show alert for unfilled fields
            showErrorAlert("Error", "Not all fields were filled.");
        }
    }

    private boolean areAllFieldsFilled() {
        // Check if all required fields are filled based on the selected product type
        if (bookRadio.isSelected()) {
            return !productTitle.getText().isEmpty() && !productQty.getText().isEmpty() &&
                    !productWeight.getText().isEmpty() && !bookAuthor.getText().isEmpty() &&
                    bookPublicDate.getValue() != null && bookGenre.getValue() != null;
        } else if (accessoriesRadio.isSelected()) {
            return !productTitle.getText().isEmpty() && !productQty.getText().isEmpty() &&
                    !productWeight.getText().isEmpty() && !accCategory.getText().isEmpty() &&
                    !accMaterial.getText().isEmpty() && !accColour.getText().isEmpty();
        } else if (stationeryRadio.isSelected()) {
            return !productTitle.getText().isEmpty() && !productQty.getText().isEmpty() &&
                    !productWeight.getText().isEmpty() && !statBrand.getText().isEmpty() &&
                    !statType.getText().isEmpty();
        } else {
            return false; // No product type selected
        }
    }

    private void showInformationAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void updateProduct() {
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        if (product instanceof Book) {
            Book book = (Book) product;
            product.setTitle(productTitle.getText());
            product.setDescription(productDescription.getText());
            product.setQty(Integer.parseInt(productQty.getText()));
            product.setWeight(Float.parseFloat(productWeight.getText()));
            book.setPublicDate(bookPublicDate.getValue());
            book.setAuthor(bookAuthor.getText());
            book.setGenre(bookGenre.getValue());
            hibernateShop.update(book);

        } else if (product instanceof Accessories) {
            Accessories acc = (Accessories) product;
            product.setTitle(productTitle.getText());
            product.setDescription(productDescription.getText());
            product.setQty(Integer.parseInt(productQty.getText()));
            product.setWeight(Float.parseFloat(productWeight.getText()));
            acc.setCategory(accCategory.getText());
            acc.setMaterial(accMaterial.getText());
            acc.setColour(accColour.getText());
            hibernateShop.update(acc);

        } else if (product instanceof Stationery) {
            Stationery stat = (Stationery) product;
            product.setTitle(productTitle.getText());
            product.setDescription(productDescription.getText());
            product.setQty(Integer.parseInt(productQty.getText()));
            product.setWeight(Float.parseFloat(productWeight.getText()));
            stat.setBrand(statBrand.getText());
            stat.setType(statType.getText());
            hibernateShop.update(stat);
        }
        showInformationAlert("Product Updated", "The product was successfully updated.");
    }

    public void deleteProduct() {
        Product product = productAdminList.getSelectionModel().getSelectedItem();
        if (product != null) {
            productAdminList.getItems().remove(product);
            hibernateShop.delete(Product.class, product.getId());
            // Show success alert
            showInformationAlert("Product Deleted", "The product was successfully deleted.");
        } else {
            // Show error alert if no product is selected
            showErrorAlert("Error", "Please select a product to delete.");
        }
    }

    public void disableFields() {
        if (bookRadio.isSelected()) {
            accCategory.setDisable(true);
            accColour.setDisable(true);
            accMaterial.setDisable(true);
            statBrand.setDisable(true);
            statType.setDisable(true);
            bookGenre.setDisable(false);
            bookAuthor.setDisable(false);
            bookPublicDate.setDisable(false);


        } else if (accessoriesRadio.isSelected()) {
            accMaterial.setDisable(false);
            accCategory.setDisable(false);
            accColour.setDisable(false);
            statBrand.setDisable(true);
            statType.setDisable(true);
            bookGenre.setDisable(true);
            bookAuthor.setDisable(true);
            bookPublicDate.setDisable(true);

        } else if (stationeryRadio.isSelected()) {
            statBrand.setDisable(false);
            statType.setDisable(false);
            accMaterial.setDisable(true);
            accCategory.setDisable(true);
            accColour.setDisable(true);
            bookGenre.setDisable(true);
            bookAuthor.setDisable(true);
            bookPublicDate.setDisable(true);

        } else {
            accMaterial.setDisable(false);
            accCategory.setDisable(false);
            accColour.setDisable(false);
            bookGenre.setDisable(false);
            bookAuthor.setDisable(false);
            bookPublicDate.setDisable(false);
            statBrand.setDisable(false);
            statType.setDisable(false);
        }
    }

    public void loadProduct() {
        Product product = productAdminList.getSelectionModel().getSelectedItem();

        if (product instanceof Book) {
            Book book = hibernateShop.getEntityById(Book.class, product.getId());
            productTitle.setText(book.getTitle());
            productDescription.setText(book.getDescription());
            productQty.setText(String.valueOf(book.getQty()));
            productWeight.setText(String.valueOf(book.getWeight()));
            bookPublicDate.setAccessibleText(String.valueOf(book.getPublicDate()));
            bookAuthor.setText(book.getAuthor());
            //bookTitle.setText(((Book) product).getTitle());
            bookGenre.setAccessibleText(String.valueOf(book.getGenre()));
        } else if (product instanceof Accessories) {
            Accessories acc = hibernateShop.getEntityById(Accessories.class, product.getId());
            productTitle.setText(acc.getTitle());
            productDescription.setText(acc.getDescription());
            productQty.setText(String.valueOf(acc.getQty()));
            productWeight.setText(String.valueOf(acc.getWeight()));
            accColour.setText(acc.getColour());
            accCategory.setText(acc.getCategory());
            accMaterial.setText(acc.getMaterial());

        } else if (product instanceof Stationery) {
            Stationery stat = hibernateShop.getEntityById(Stationery.class, product.getId());
            productTitle.setText(stat.getTitle());
            productDescription.setText(stat.getDescription());
            productQty.setText(String.valueOf(stat.getQty()));
            productWeight.setText(String.valueOf(stat.getWeight()));
            statBrand.setText(stat.getBrand());
            statType.setText(stat.getType());
        }
    }

    public void LeaveAReview() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("product-review.fxml"));
        Parent parent = fxmlLoader.load();

        ProductReview productReview = fxmlLoader.getController();
        productReview.setData(entityManagerFactory, user);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
        /*stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();*/
    }

    public void viewProduct() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        Product product = hibernateShop.getEntityById(Product.class, shopProducts.getSelectionModel().getSelectedItem().getId());
        if (product instanceof Book) {
            Book book = (Book) product;
            alert.setHeaderText(book.getTitle());
            alert.setContentText(book.toString());
        } else if(product instanceof Accessories){
            Accessories acc = (Accessories) product;
            alert.setHeaderText(acc.getTitle());
            alert.setContentText(acc.toString());
        } else if(product instanceof Stationery){
            Stationery stat = (Stationery) product;
            alert.setHeaderText(stat.getTitle());
            alert.setContentText(stat.toString());
        }
        alert.showAndWait();
    }

    public void loadTabData() {
        if (shopTab.isSelected()) {
            shopProducts.getItems().addAll(hibernateShop.loadAvailableProducts());
        } else if (usersTab.isSelected()) {
            /*fillManagerTable();
            List<Customer> customers = hibernateShop.getAllRecords(Customer.class);
            ObservableList<CustomerTableParameters> CustomersToDisplay = fillCustomerTable(customers);
            customerTable.setItems(CustomersToDisplay);*/
        } else if (ordersTab.isSelected()) {
            //ordersList.getItems().addAll(hibernateShop.loadAvailableCarts());
        } else if (warehousesTab.isSelected()){
            warehouseList.getItems().addAll(hibernateShop.getAllRecords(Warehouse.class));
        } else if (productTab.isSelected()){
            productAdminList.getItems().addAll(hibernateShop.loadAvailableProducts());
        }
    }

    public void buyProducts() {
        List<Product> cartProducts = viewProducts.getItems();
        hibernateShop.createCart(cartProducts, user);
    }

    public void addToCart() {
        Product product = shopProducts.getSelectionModel().getSelectedItem();
        viewProducts.getItems().add(product);
        shopProducts.getItems().remove(product);
    }

    public void removeFromCart() {
        Product product = viewProducts.getSelectionModel().getSelectedItem();
        shopProducts.getItems().add(product);
        viewProducts.getItems().remove(product);
    }

    public void createManager() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("registration.fxml"));
        Parent parent = fxmlLoader.load();

        //Access controller of main window. Each form has its own controller, so make sure that you make no mistake here
        Registration registration = fxmlLoader.getController();
        registration.setData(entityManagerFactory, true, false);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, false);
    }

    public void deleteOrder() {
        ObservableList<Cart> selectedOrders = ordersList.getSelectionModel().getSelectedItems();

        for (Cart selectedOrder : selectedOrders) {
            // Remove the selected order from the database
            genericHibernate.delete(Cart.class, selectedOrder.getId());
        }

        // Remove the selected orders from the ListView
        ordersList.getItems().removeAll(selectedOrders);
    }

    public void viewWarehouse() {
        Warehouse warehouse = warehouseList.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(String.valueOf(warehouse.getCity()));
            alert.setContentText(warehouse.toString());
            alert.showAndWait();
    }

    public void addWarehouse() {
        Warehouse warehouse = new Warehouse(new ArrayList<>());
        warehouse = new Warehouse(warehouseAddress.getText(),
                warehouseCity.getValue());
        hibernateShop.create(warehouse);
        warehouseList.getItems().clear();
        warehouseList.getItems().addAll(hibernateShop.getAllRecords(Warehouse.class));
    }

    public void deleteWarehouse() {
        Warehouse warehouse = warehouseList.getSelectionModel().getSelectedItem();
        warehouseList.getItems().remove(warehouse);
        hibernateShop.delete(Warehouse.class, warehouse.getId());
    }

    public void loadProducts() {
        Warehouse warehouse = hibernateShop.getEntityById(Warehouse.class, warehouseList.getSelectionModel().getSelectedItem().getId());

        // Create a new root if the current root is null
        if (warehouseProducts.getRoot() == null) {
            warehouseProducts.setRoot(new TreeItem<>());
            warehouseProducts.setShowRoot(false);
            warehouseProducts.getRoot().setExpanded(true);
        }

        // Clear existing children
        warehouseProducts.getRoot().getChildren().clear();

        // Add products as children
        warehouse.getProductList().forEach(product -> addTreeItem(product, warehouseProducts.getRoot()));
    }

    private void addTreeItem(Product product, TreeItem<Product> warehouse) {
        TreeItem<Product> treeItem = new TreeItem<>(product);
        warehouse.getChildren().add(treeItem);
    }

    public void addMessage() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("message.fxml"));
        Parent parent = fxmlLoader.load();

        Message message = fxmlLoader.getController();
        message.setData(entityManagerFactory, user, ordersList.getSelectionModel().getSelectedItem());
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    public void loadChat() {
        Cart cart = hibernateShop.getEntityById(Cart.class, ordersList.getSelectionModel().getSelectedItem().getId());
        orderChat.setRoot(new TreeItem<>());
        orderChat.setShowRoot(false);
        orderChat.getRoot().setExpanded(true);
        cart.getChat().forEach(comment -> addTreeItem(comment, orderChat.getRoot()));
    }

    public void reply() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("message.fxml"));
        Parent parent = fxmlLoader.load();
        Message message = fxmlLoader.getController();
        message.setData(entityManagerFactory, orderChat.getSelectionModel().getSelectedItem().getValue(), user);
        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        fxUtils.setStageParameters(stage, scene, true);
    }

    private void addTreeItem(Comment comment, TreeItem<Comment> parentComment) {
        TreeItem<Comment> treeItem = new TreeItem<>(comment);
        parentComment.getChildren().add(treeItem);
        comment.getReplies().forEach(sub -> addTreeItem(sub, treeItem));
    }
}
