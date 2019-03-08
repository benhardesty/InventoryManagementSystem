/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package benhardestyinventorysystem.view_controller;

import benhardestyinventorysystem.util.AlertUtil;
import java.util.Optional;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

/**
 *
 * @author Ben Hardesty
 */
public class RootSceneController {
    
    private Stage stage;
    
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
     * Called by the main application to give this controller access to it's 
     * stage.
     * 
     * @param stage 
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
