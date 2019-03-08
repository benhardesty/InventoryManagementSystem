/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem;

import benhardestyinventorysystem.model.Inhouse;
import benhardestyinventorysystem.model.Inventory;
import benhardestyinventorysystem.model.Outsourced;
import benhardestyinventorysystem.model.Part;
import benhardestyinventorysystem.model.Product;
import benhardestyinventorysystem.view_controller.AddOrEditPartController;
import benhardestyinventorysystem.view_controller.AddOrEditProductController;
import benhardestyinventorysystem.view_controller.MainSceneController;
import benhardestyinventorysystem.view_controller.RootSceneController;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author Ben Hardesty
 */
public class Main extends Application {
    
    private Stage primaryStage;
    private BorderPane rootLayout;
    private Inventory inventory;
    
    /**
     * Default constructor.
     * Load initial data into the inventory management system.
     */
    public Main() {
        inventory = new Inventory();
        Inhouse inhousePart = new Inhouse(inventory.getParts().size(), "Bolt", 5.00, 5, 0, 5, 1);
        inventory.addPart(inhousePart);
        Outsourced outsourcedPart = new Outsourced(inventory.getParts().size(), "Nail", 5.00, 5, 0, 5, "Company");
        inventory.addPart(outsourcedPart);
        Product product = new Product(0, "Toolbox", 10, 1, 0, 5, inventory.getParts());
        inventory.addProduct(product);
    }
    
    @Override
    public void start(Stage primaryStage) {
        
        this.primaryStage = primaryStage;
        loadRootLayout();
        loadMainScene();
    }
    
    /**
     * Load a BorderPane layout as the root layout of the application.
     */
    public void loadRootLayout() {
        try {
            // Load the new scene
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/RootLayout.fxml"));
            rootLayout = (BorderPane) loader.load();
            
            // Give the controller a reference to the stage just created for it.
            RootSceneController controller = loader.getController();
            controller.setStage(primaryStage);
            
            Scene scene = new Scene(rootLayout);
            
            primaryStage.setTitle("Inventory Management System");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the mainScene to the center of {@link Main#rootLayout}.
     */
    public void loadMainScene() {
        try {
            // Load the new scene
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/MainScene.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();

            // Set the new scene into the center of the root layout.
            rootLayout.setCenter(pane);
            
            // Give the controller for the new scene access to the current stage.
            MainSceneController controller = loader.getController();
            controller.setMain(this, primaryStage, inventory);
        } catch (IOException e) {
            
        }
    }
    
    /**
     * Load the add/edit part scene into it's own stage.
     * 
     * @param part
     * @return a Map with key "saved" to indicate whether the part was successfully
     * saved and key "part" with a reference to the part Object.
     */
    public Map loadAddOrEditPartScene(Part part) {
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/AddOrEditPart.fxml"));
            AnchorPane pane = (AnchorPane) loader.load();
            
            // Create a new stage for this scene.
            Stage stage = new Stage();
            stage.setTitle("Inventory Management System - Add Part");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage just created for it
            // along with the part passed to this method and a reference to
            // the main application.
            AddOrEditPartController controller = loader.getController();
            controller.setStage(stage, this, part, inventory);
            
            // Show the scene and wait for it to finish.
            stage.showAndWait();
            
            // Return a map indicating whether the part was saved successfully and
            // returning a reference to the new part.
            Map<String, Object> map = new HashMap<>();
            map.put("saved", controller.partSaved());
            map.put("part", controller.getPart());
            return map;
        } catch (IOException e) {
            return null;
        }
    }
    
    /**
     * Load the add/edit product scene into it's own stage.
     * 
     * @param product
     * @return a Map with key "saved" to indicate whether the product was successfully
     * saved and key "product" with a reference to the product Object.
     */
    public Map loadAddOrEditProductScene(Product product) {
        
        try {
            // Load the fxml for the new scene.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("view_controller/AddOrEditProduct.fxml"));
            AnchorPane pane = loader.load();
            
            // Create a stage for the new scene.
            Stage stage = new Stage();
            stage.initOwner(primaryStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setTitle("Inventory Management System - Add Product");
            Scene scene = new Scene(pane);
            stage.setScene(scene);
            
            // Give the controller a reference to the stage just created for it
            // along with the product passed to this method and a reference to 
            // the main application.
            AddOrEditProductController controller = loader.getController();
            controller.setStage(stage, this, product, inventory);
            
            // Show the scene and wait for it to finish.
            stage.showAndWait();
            
            // Return a map indicating whether the product was saved successfully
            // and returning a reference to the new part.
            Map<String, Object> map = new HashMap<>();
            map.put("saved", controller.productSaved());
            map.put("product", controller.getProduct());
            return map;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
