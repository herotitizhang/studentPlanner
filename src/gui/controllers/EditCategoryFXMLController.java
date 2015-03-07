/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import gui.ItemNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.CategoryI;
import model.NameInUseException;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class EditCategoryFXMLController implements Initializable {

    @FXML TextField nameInput;
    @FXML Label errorLabel;
    
    /**
     * Triggered when user submits category information
     */
    @FXML
    public void handleSubmitButtonAction() {
        try {
            DataHandler.getInstance().updateCurrentCategory(nameInput.getText());
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/DashboardViewFXML.fxml"));
            Parent root = (Parent) loader.load();
            DashboardViewFXMLController controller = loader.getController();
            controller.resetCategoryListView();
            Scene scene = nameInput.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        } catch (ItemNotFoundException ex) {
            Logger.getLogger(EditCategoryFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EditCategoryFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            nameInput.setText(DataHandler.getInstance().getCurrentCategory().toString());
        } catch (ItemNotFoundException ex) {
            Logger.getLogger(EditCategoryFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
}
