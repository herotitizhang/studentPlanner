/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EventI;
import model.EventI.Priority;
import model.EventI.Repeat;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class AddEventFXMLController implements Initializable {

    @FXML TextField nameInput;
    @FXML TextField textInput;
    @FXML TextField startTimeInput;
    @FXML TextField endTimeInput;
    @FXML CheckBox alertBoolInput;
    @FXML TextField alertTimeInput;
    @FXML TextField alertTextInput;
    @FXML TextField categoryInput;
    @FXML Button submitButton;
    
    /**
     * Triggered when user submits event information
     */
    @FXML
    private void handleSubmitButtonAction() {
        if (DataHandler.getInstance().addEvent(nameInput.getText(), textInput.getText(), startTimeInput.getText(), 
                endTimeInput.getText(), alertBoolInput.isSelected(), alertTextInput.getText(), alertTimeInput.getText(), 
                Repeat.NONE, Priority.HIGH, DataHandler.getInstance().getCategory(categoryInput.getText()))) {
            Scene scene = submitButton.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        } else {
            // handle case where event couldn't be added
            // use exceptions here instead of if else
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }    
    
}
