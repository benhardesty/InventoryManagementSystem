/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.view_controller;

import benhardestyinventorysystem.Main;
import benhardestyinventorysystem.model.Inhouse;
import benhardestyinventorysystem.model.Inventory;
import benhardestyinventorysystem.model.Outsourced;
import benhardestyinventorysystem.model.Part;
import benhardestyinventorysystem.util.AlertUtil;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Ben Hardesty
 */
public class AddOrEditPartController implements Initializable {
    
    private Stage stage;
    private Part part;
    private Main main;
    private Inventory inventory;
    private boolean partSaved;
    private ToggleGroup toggleGroup;
    
    @FXML
    private RadioButton inhouseRadioButton;
    @FXML
    private RadioButton outsourcedRadioButton;
    
    @FXML
    private Label sceneTitle;
    @FXML
    private TextField partIDTextField;
    @FXML
    private TextField partNameTextField;
    @FXML
    private TextField partInventoryTextField;
    @FXML
    private TextField partMinInventoryTextField;
    @FXML
    private TextField partMaxInventoryTextField;
    @FXML
    private TextField partPriceTextField;
    @FXML
    private Label companyOrMachineIDLabel;
    @FXML
    private TextField companyOrMachineIDTextField;
    
    @FXML
    private Button saveButton;
    @FXML
    private Button cancelButton;
    
    /**
     * Validate the input and create a new part.
     */
    @FXML
    private void handleSave() {
        if (validInput()) {
            try {
                Integer partID = ((part == null) ? inventory.getParts().size() : part.getPartID());
                String name = partNameTextField.getText();
                Integer curInv = Integer.parseInt(partInventoryTextField.getText());
                Integer minInv = Integer.parseInt(partMinInventoryTextField.getText());
                Integer maxInv = Integer.parseInt(partMaxInventoryTextField.getText());
                Double price = Double.parseDouble(partPriceTextField.getText());

                if (toggleGroup.getSelectedToggle() == inhouseRadioButton) {
                    Integer machineID = Integer.parseInt(companyOrMachineIDTextField.getText());
                    part = new Inhouse(partID, name, price, curInv, minInv, maxInv, machineID);
                } else {
                    String companyName = companyOrMachineIDTextField.getText();
                    part = new Outsourced(partID, name, price, curInv, minInv, maxInv, companyName);
                }

                // Set partSaved to true and close the stage.
                partSaved = true;
                stage.close();
            } catch (NumberFormatException e) {
                AlertUtil.showErrorAlert(stage, "Error", "Invalid Input",
                        "Current, Min, and Max Inventory must be integers.\n"
                                + "Price must be a number.\n");
            }
            
        }
    }
    
    /**
     * If the user attempts to cancel adding or modifying a part, present a 
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
     * Validate the form input.
     * 
     * @return true if input is valid, false otherwise.
     */
    private boolean validInput() {
        boolean valid = true;
        String errorMessage = "";
        
        if (partNameTextField.getText().isEmpty() || partNameTextField.getText().equals("")) {
            valid = false;
            errorMessage += "Name field cannot be blank.\n";
        }
        
        if (!partInventoryTextField.getText().isEmpty() && !partInventoryTextField.getText().equals("") &&
            !partMinInventoryTextField.getText().isEmpty() && !partMinInventoryTextField.getText().equals("") &&
            !partMaxInventoryTextField.getText().isEmpty() && !partMaxInventoryTextField.getText().equals("")) {
            
            try {
                Integer currentInventory = Integer.parseInt(partInventoryTextField.getText());
                Integer minInventory = Integer.parseInt(partMinInventoryTextField.getText());
                Integer maxInventory = Integer.parseInt(partMaxInventoryTextField.getText());
                
                if (currentInventory < minInventory || currentInventory > maxInventory) {
                    throw new Exception("Current Inventory must be greater than or equal\n"
                            + "to Min Inventory and less than or equal to Max\n"
                            + "Inventory.\n");
                }
                
            } catch (NumberFormatException e) {
                valid = false;
                errorMessage += "Current, Max, and Min Inventory must all be\n"
                        + "integers.\n";
            } catch (Exception e) {
                valid = false;
                errorMessage += e.getMessage();
            }
        } else {
            valid = false;
            errorMessage += "Current Inventory, Max Inventory, and Min Inventory\n"
                        + "cannot be blank.\n";
        }
        
        try {
            Double.parseDouble(partPriceTextField.getText());
        } catch (NumberFormatException e) {
            valid = false;
            errorMessage += "Price must be a number.\n";
        }
        
        if (toggleGroup.getSelectedToggle() == inhouseRadioButton) {
            try {
                if (companyOrMachineIDTextField.getText().isEmpty() || companyOrMachineIDTextField.getText().equals("")) {
                    valid = false;
                    errorMessage += "Machine ID cannot be blank.\n";
                }
                Integer machineID = Integer.parseInt(companyOrMachineIDTextField.getText());
                
            } catch (NumberFormatException e) {
                valid = false;
                errorMessage += "MachineID must be an Integer.\n";
            }
        } else {
            if (companyOrMachineIDTextField.getText().isEmpty() || companyOrMachineIDTextField.getText().equals("")) {
                valid = false;
                errorMessage += "Company name cannot be blank.\n";
            }
        }
        
        if (valid) {
            return true;
        } else {
            AlertUtil.showErrorAlert(stage, "Error", 
                    "Please correct the invalid fields", errorMessage);
            return false;
        }
    }
    
    /**
     * Called by the main application to give this controller a reference to
     * it's stage, the part to be edited, the main application, and the inventory
     * from the main application. If the provided part is not null, the part 
     * attributes will be loaded to the scene.
     * 
     * @param stage
     * @param part
     * @param main
     * @param inventory
     */
    public void setStage(Stage stage, Main main, Part part, Inventory inventory) {
        this.stage = stage;
        this.main = main;
        this.part = part;
        this.inventory = inventory;
        
        if (part != null) {
            this.stage.setTitle("Inventory Management System - Modify Part");
            sceneTitle.setText("Modify Part");
            partIDTextField.setText(String.valueOf(part.getPartID()));
            partNameTextField.setText(part.getName());
            partInventoryTextField.setText(String.valueOf(part.getInStock()));
            partMinInventoryTextField.setText(String.valueOf(part.getMin()));
            partMaxInventoryTextField.setText(String.valueOf(part.getMax()));
            partPriceTextField.setText(String.valueOf(part.getPrice()));
            
            if (part.getClass() == Inhouse.class) {
                companyOrMachineIDLabel.setText("Machine ID");
                companyOrMachineIDTextField.setText(String.valueOf(((Inhouse) part).getMachineID()));
                inhouseRadioButton.setSelected(true);
            } else {
                companyOrMachineIDLabel.setText("Company Name");
                companyOrMachineIDTextField.setText(String.valueOf(((Outsourced) part).getCompanyName()));
                outsourcedRadioButton.setSelected(true);
            }
        } else {
            // Set default inventory to 0.
            partInventoryTextField.setText("0");
        }
    }
    
    /**
     * Return whether or not the part was successfully saved. Called by the
     * main application.
     * 
     * @return 
     */
    public boolean partSaved() {
        return partSaved;
    }
    
    /**
     * Return the part. Called by the main application.
     * 
     * @return 
     */
    public Part getPart() {
        return part;
    }
    
    /**
     * Change the text of {@link AddPartController#companyOrMachineIDLabel} 
     * based on if the part is made in-house or outsourced. Also updates the
     * text field for the machine ID or company name if the part is not null.
     * 
     * @param toggle 
     */
    private void handleRadioButtonToggle(Toggle toggle) {
        if (toggle == inhouseRadioButton) {
            companyOrMachineIDLabel.setText("Machine ID");
            if (part != null) {
                if (part.getClass() == Inhouse.class) {
                    companyOrMachineIDTextField.setText(Integer.toString(((Inhouse) part).getMachineID()));
                } else {
                    companyOrMachineIDTextField.setText("");
                }
            }
        } else {
            companyOrMachineIDLabel.setText("Company Name");
            if (part != null) {
                if (part.getClass() == Outsourced.class) {
                    companyOrMachineIDTextField.setText(((Outsourced) part).getCompanyName());
                } else {
                    companyOrMachineIDTextField.setText("");
                }
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        part = null;
        partSaved = false;
        
        // Initialize toggle group for inhouse or outsourced radio buttons.
        toggleGroup = new ToggleGroup();
        inhouseRadioButton.setToggleGroup(toggleGroup);
        inhouseRadioButton.setUserData("inhouse");
        inhouseRadioButton.setSelected(true);
        companyOrMachineIDLabel.setText("Machine ID");
        outsourcedRadioButton.setToggleGroup(toggleGroup);
        outsourcedRadioButton.setUserData("outsourced");
        
        // Add a listener to the toggle group.
        toggleGroup.selectedToggleProperty().addListener(
                (observable, oldValue, newValue) -> 
                        handleRadioButtonToggle(toggleGroup.getSelectedToggle())
        );
    }
    
}
