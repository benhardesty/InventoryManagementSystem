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
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class MainSceneController implements Initializable {
    
    private Main main;
    private Inventory inventory;
    private Stage stage;
    
    @FXML
    private Button partSearchButton;
    @FXML
    private TextField partSearchTextField;
    @FXML
    private Button partClearSearchButton;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIDColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInventoryColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    
    @FXML
    private Button productSearchButton;
    @FXML
    private TextField productSearchTextField;
    @FXML
    private Button productClearSearchButton;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIDColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInventoryColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    
    /**
     * Load a new stage to add a part. A null pointer is passed to the scene
     * since this will be a new part that is created. The function returns a map
     * with key "saved" indicating whether the new part was saved and "part" with
     * the new part.
     */
    @FXML
    private void handleAddPart() {
        Map result = main.loadAddOrEditPartScene(null);
        
        if (((boolean) result.get("saved")) && ((Part) result.get("part")) != null) {
            Part part = (Part) result.get("part");
            inventory.addPart(part);
        }
    }
    
    /**
     * Modify an existing part. An existing part object is passed to the 
     * AddOrEditPartController controller class and a map is returned.
     */
    @FXML
    private void handleModifyPart() {
        int index = partTable.getSelectionModel().getSelectedIndex();
        
        if (index > -1) {
            Map result = main.loadAddOrEditPartScene(inventory.getParts().get(index));

            if (((boolean) result.get("saved")) && ((Part) result.get("part")) != null) {
                Part part = (Part) result.get("part");
                inventory.updatePart(index, part);
            }
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Part Selected"
                    , "Please select a part to modify.");
        }
    }
    
    /**
     * Delete a part from the inventory.
     */
    @FXML
    private void handleDeletePart() {
        int index = partTable.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Part part = inventory.lookupPart(index);
            inventory.deletePart(part);
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Part Selected"
                    , "Please select a part to delete.");
        }
    }
    
    /**
     * Load a new stage to add a product. A null pointer is passed to the scene
     * since this will be a new product that is created. The function returns a map
     * with key "saved" indicating whether the new product was saved and key 
     * "product" with the new product.
     */
    @FXML
    private void handleAddProduct() {
        Map result = main.loadAddOrEditProductScene(null);
        
        if (((boolean) result.get("saved")) && ((Product) result.get("product")) != null) {
            Product product = (Product) result.get("product");
            inventory.addProduct(product);
        }
    }
    
    /**
     * Modify an existing product. An existing product object is passed to the 
     * AddOrEditProductController controller class and a map is returned.
     */
    @FXML
    private void handleModifyProduct() {
        int index = productTable.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Map result = main.loadAddOrEditProductScene(inventory.lookupProduct(index));

            if (((boolean) result.get("saved")) && ((Product) result.get("product")) != null) {
                Product product = (Product) result.get("product");
                inventory.updateProduct(index, product);
            }
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Product Selected"
                    , "Please select a product to modify.");
        }
    }
    
    /**
     * Delete a product from the inventory.
     */
    @FXML
    private void handleDeleteProduct() {
        int index = productTable.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            inventory.removeProduct(index);
        } else {
            AlertUtil.showErrorAlert(stage, "Error", "No Product Selected"
                    , "Please select a product to delete.");
        }
    }
    
    /**
     * Close the application.
     */
    @FXML
    private void handleExit() {
        Optional<ButtonType> result = AlertUtil.showConfirmationAlert(stage, 
                "Confirmation", "Click 'OK' to confirm you wish to close\n"
                        + "the application.");
        if (result.get() == ButtonType.OK) {
            Platform.exit();
        }
    }
    
    /**
     * Called by the main application to supply a reference to itself.
     * Set the items in the parts and products tables using the inventory
     * object from the main application and add search functionality to both
     * tables.
     * 
     * @param main 
     * @param stage
     * @param inventory
     */
    public void setMain(Main main, Stage stage, Inventory inventory) {
        this.main = main;
        this.stage = stage;
        this.inventory = inventory;
        
        partTable.setItems(inventory.getParts());
        productTable.setItems(inventory.getProducts());
        
        // Set initial part table filter predicate to true.
        FilteredList<Part> filteredParts = new FilteredList<>(inventory.getParts(),p -> true);
        SortedList<Part> sortedParts = new SortedList<>(filteredParts);
        sortedParts.comparatorProperty().bind(partTable.comparatorProperty());
        partTable.setItems(sortedParts);
        
        // Update part table filter predicate when search button is clicked.
        partSearchButton.setOnAction(action -> {
            filteredParts.setPredicate(part -> {
                // Check if each item contains the text in the search field.
                return part.getName().toLowerCase().contains(partSearchTextField.getText().toLowerCase());
            });
        });
        
        // Clear part table filter when clear button is clicked.
        partClearSearchButton.setOnAction(action -> {
            partSearchTextField.setText("");
            filteredParts.setPredicate(part -> true);
        });
        
        // Set initial product table filter predicate to true.
        FilteredList<Product> filteredProducts = new FilteredList<>(inventory.getProducts(),p -> true);
        SortedList<Product> sortedProducts = new SortedList<>(filteredProducts);
        sortedProducts.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedProducts);
        
        // Update product table filter predicate when search button is clicked.
        productSearchButton.setOnAction(action -> {
            filteredProducts.setPredicate(product -> {
                // Check if each item contains the text in the search field.
                return product.getName().toLowerCase().contains(productSearchTextField.getText().toLowerCase());
            });
        });
        
        // Clear product table filter when clear button is clicked.
        productClearSearchButton.setOnAction(action -> {
            productSearchTextField.setText("");
            filteredProducts.setPredicate(product -> true);
        });
    }

    /**
     * Initializes the controller class.
     * Initialize the parts and products tables.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // Initialize the part table.
        partIDColumn.setCellValueFactory(cellData -> cellData.getValue().partIDProperty().asObject());
        partNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        partInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        partPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
        
        // Initialize the products table.
        productIDColumn.setCellValueFactory(cellData -> cellData.getValue().productIDProperty().asObject());
        productNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        productInventoryColumn.setCellValueFactory(cellData -> cellData.getValue().inStockProperty().asObject());
        productPriceColumn.setCellValueFactory(cellData -> cellData.getValue().priceProperty().asObject());
    }    
    
}
