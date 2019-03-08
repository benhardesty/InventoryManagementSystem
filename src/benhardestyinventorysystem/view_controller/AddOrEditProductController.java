/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.view_controller;

import benhardestyinventorysystem.Main;
import benhardestyinventorysystem.model.Inventory;
import benhardestyinventorysystem.model.Part;
import benhardestyinventorysystem.model.Product;
import benhardestyinventorysystem.util.AlertUtil;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class AddOrEditProductController implements Initializable {
    
    Main main;
    Inventory inventory;
    Stage stage;
    Product product;
    boolean productSaved;
    ObservableList<Part> associatedParts = FXCollections.observableArrayList();
    
    @FXML
    private Label sceneTitleLabel;
    
    @FXML
    private Button searchPartsButton;
    @FXML
    private TextField searchPartsTextField;
    @FXML
    private Button clearSearchPartsButton;
    @FXML
    private TableView<Part> allPartsTable;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    
    @FXML
    private TableView<Part> associatedPartsTable;
    @FXML
    private TableColumn<Part, Integer> associatedPartIDColumn;
    @FXML
    private TableColumn<Part, String> associatedPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> associatedPartInventoryColumn;
    @FXML
    private TableColumn<Part, Double> associatedPartPriceColumn;
    
    @FXML
    private TextField productIDTextField;
    @FXML
    private TextField productNameTextField;
    @FXML
    private TextField productCurrentInventoryTextField;
    @FXML
    private TextField productMinInventoryTextField;
    @FXML
    private TextField productMaxInventoryTextField;
    @FXML
    private TextField productPriceTextField;
    
    /**
     * Add a part from the all parts table to the product-associated parts table.
     */
    @FXML
    private void handleAdd() {
        Integer index = allPartsTable.getSelectionModel().getSelectedIndex();
        
        if (index > -1) {
            associatedParts.add(inventory.getParts().get(index));
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Part Selected","Please select a part to add.");
        }
    }
    
    /**
     * Delete a part from the product-associated parts table.
     */
    @FXML
    private void handleDelete(){
        Integer index = associatedPartsTable.getSelectionModel().getSelectedIndex();
        
        if (index > -1) {
            associatedParts.remove(associatedParts.get(index));
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Part Selected", "Please select a part to remove.");
        }
    }
    
    /**
     * Create/Update a product when the user selects 'Save'.
     */
    @FXML
    private void handleSave(){
        if (validInput()) {
            
            try {
                Integer ID = ((product == null) ? inventory.getProducts().size() : product.getProductID());
                String name = productNameTextField.getText();
                Integer currentInventory = Integer.parseInt(productCurrentInventoryTextField.getText());
                Integer minInventory = Integer.parseInt(productMinInventoryTextField.getText());
                Integer maxInventory = Integer.parseInt(productMaxInventoryTextField.getText());
                Double price = Double.parseDouble(productPriceTextField.getText());

                product = new Product(ID, name, price, currentInventory, minInventory, maxInventory, associatedParts);
                productSaved = true;
                stage.close();
            } catch (NumberFormatException e) {
                AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", 
                        "Current, Min, and Max Inventory must be integers.\n"
                                + "Price must be a number.\n");
            }
        }
    }
    
    /**
     * If the user attempts to cancel creating/editing a product, show a 
     * confirmation screen.
     */
    @FXML
    private void handleCancel() {
        
        Optional<ButtonType> result = AlertUtil.showConfirmationAlert(stage, 
                "Confirm", "Click 'OK' to confirm you want to cancel.");
        
        if (result.get() == ButtonType.OK) {
            stage.close();
        }
    }
    
    /**
     * Validate product information.
     * 
     * @return true if information is valid, false otherwise.
     */
    private boolean validInput() {
        boolean valid = true;
        String errorMessage = "";
        
        if (productNameTextField.getText().isEmpty() || productNameTextField.getText().equals("") ||
                productCurrentInventoryTextField.getText().isEmpty() || productCurrentInventoryTextField.getText().equals("") ||
                productMinInventoryTextField.getText().isEmpty() || productMinInventoryTextField.getText().equals("") ||
                productMaxInventoryTextField.getText().isEmpty() || productMaxInventoryTextField.getText().equals("") ||
                productPriceTextField.getText().isEmpty() || productPriceTextField.getText().equals("")) {
            valid = false;
            errorMessage += "All fields must be filled out.\n";
        }
        
        try {
            Integer curInv = Integer.parseInt(productCurrentInventoryTextField.getText());
            Integer minInv = Integer.parseInt(productMinInventoryTextField.getText());
            Integer maxInv = Integer.parseInt(productMaxInventoryTextField.getText());
            
            if (curInv < minInv || curInv > maxInv) {
                throw new Exception("Current Inventory must be greater than or equal\n"
                        + "to Min Inventory and less than or equal to Max\n"
                        + "Inventory.\n");
            }
        } catch (NumberFormatException e) {
            valid = false;
            errorMessage += "Current, Min, and Max Inventory must all be\n"
                    + "integers.\n";
        } catch (Exception e) {
            valid = false;
            errorMessage += e.getMessage();
        }
        
        try {
            Double price = Double.parseDouble(productPriceTextField.getText());
            
            double sum = 0;
            for (int i = 0; i < associatedParts.size(); i++) {
                sum += associatedParts.get(i).getPrice();
            }
            
            if (price < sum) {
                throw new Exception("The price of the product cannot be less than\n"
                        + "the price of all it's parts.\n");
            }
        } catch (NumberFormatException e) {
            valid = false;
            errorMessage += "Price must be a number.\n";
        } catch (Exception e) {
            valid = false;
            errorMessage += e.getMessage();
        }
        
        if (associatedParts.size() <= 0) {
            valid = false;
            errorMessage += "At least one part is required.\n";
        }
        
        if (valid) {
            return valid;
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "Invalid Input", errorMessage);
            return valid;
        }
    }
    
    /**
     * Called by the main application to send the product object back to the
     * main scene.
     * 
     * @return Product
     */
    public Product getProduct() {
        return product;
    }
    
    /**
     * Called by the main application to determine if the product was saved
     * successfully.
     * 
     * @return boolean
     */
    public boolean productSaved() {
        return productSaved;
    }
    
    /**
     * Called by the main application to give the controller access to it's stage,
     * the main application, a product Object, and the application inventory object.
     * A search filter is set up on the all parts table now that this controller
     * has access to the parts data.
     * 
     * @param stage
     * @param main
     * @param product
     * @param inventory
     */
    public void setStage(Stage stage, Main main, Product product, Inventory inventory) {
        this.stage = stage;
        this.main = main;
        this.product = product;
        this.inventory = inventory;
        
        allPartsTable.setItems(inventory.getParts());
        
        if (product != null) {
            this.stage.setTitle("Inventory Management System - Modify Product");
            sceneTitleLabel.setText("Modify Product");
            productIDTextField.setText(String.valueOf(product.getProductID()));
            productNameTextField.setText(product.getName());
            productCurrentInventoryTextField.setText(String.valueOf(product.getInStock()));
            productMinInventoryTextField.setText(String.valueOf(product.getMin()));
            productMaxInventoryTextField.setText(String.valueOf(product.getMax()));
            productPriceTextField.setText(String.valueOf(product.getPrice()));
            associatedParts.addAll(product.getAssociatedParts());
        } else {
            productCurrentInventoryTextField.setText("0");
        }
        
        // Set initial table filter predicate to true.
        FilteredList<Part> filteredParts = new FilteredList<>(inventory.getParts(),p -> true);
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(allPartsTable.comparatorProperty());
        allPartsTable.setItems(sortedParts);
        
        // Update table filter predicate when search button is clicked.
        searchPartsButton.setOnAction(action -> {
            filteredParts.setPredicate(part -> {
                // Check if each item contains the text in the search field.
                return part.getName().toLowerCase().contains(searchPartsTextField.getText().toLowerCase());
            });
        });
        
        // Clear part table filter when clear button is clicked.
        clearSearchPartsButton.setOnAction(action -> {
            searchPartsTextField.setText("");
            filteredParts.setPredicate(part -> true);
        });
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        productSaved = false;
        
        // Initialize allParts Table.
        partIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        
        // Initialize associatedParts Table.
        associatedPartsTable.setItems(associatedParts);
        associatedPartIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        associatedPartNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        associatedPartInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        associatedPartPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }    
    
}
