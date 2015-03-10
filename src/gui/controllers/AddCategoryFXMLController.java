/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.ApplicationControl;
import gui.DataHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.CategoryI;
import model.NameInUseException;

/**
 * FXML Controller class for add category dialog
 *
 * @author anniefifer1
 */
public class AddCategoryFXMLController implements Initializable {
    
    @FXML TextField nameInput;
    @FXML Label errorLabel;
    
    /**
     * Triggered when user submits category information
     */
    @FXML
    public void handleSubmitButtonAction() {
        try {
            CategoryI newCat = DataHandler.getInstance().addCategory(nameInput.getText());
            ApplicationControl.getInstance().closeWindow(nameInput);
        } catch (NameInUseException ex) {
            errorLabel.setText("Name in use, please use another name");
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
