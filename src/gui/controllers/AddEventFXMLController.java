/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import gui.EmptyFieldException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EventI.Priority;
import model.EventI.Repeat;
import model.NameInUseException;

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
    @FXML ComboBox<Repeat> repeatInput;
    @FXML ComboBox<Priority> priorityInput;
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
        try {
            DataHandler.getInstance().addEvent(nameInput.getText(), 
                    textInput.getText(), startTimeInput.getText(), endTimeInput.getText(), alertBoolInput.isSelected(), 
                    alertTextInput.getText(), alertTimeInput.getText(), repeatInput.getValue(), 
                    priorityInput.getValue(), DataHandler.getInstance().getCategory(categoryInput.getText()));
            Scene scene = submitButton.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        } catch (EmptyFieldException ex) {
            openSimpleDialog("Event needs a name, start time and end \n time.");
        }    
    }
    
    private void openSimpleDialog(String msg) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/SimpleDialogFXML.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene((Parent) loader.load()));
            SimpleDialogFXMLController controller = loader.<SimpleDialogFXMLController>getController();
            controller.setMessage(msg);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(AddEventFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Restricts alert information input until alert is checked
     */
    private void initInputRestrictions() {
        alertTextInput.setEditable(false);
        alertTimeInput.setEditable(false);
        alertBoolInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (alertBoolInput.isSelected()) {
                    alertTextInput.setEditable(true);
                    alertTimeInput.setEditable(true);
                } else {
                    alertTextInput.setEditable(false);
                    alertTimeInput.setEditable(false);
                    alertTextInput.clear();
                    alertTimeInput.clear();
                }
            }
        });
    }
    
    /**
     * Fills repeatInput ComboBox
     */
    private void initRepeatOptions() {
        repeatInput.setItems(FXCollections.observableArrayList(Repeat.values()));
        repeatInput.setValue(Repeat.NONE);
    }
    
    /**
     * Fills priorityInput ComboBox
     */
    private void initPriorityOptions() {
        priorityInput.setItems(FXCollections.observableArrayList(Priority.values()));
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initRepeatOptions();
        initPriorityOptions();
        initInputRestrictions();
    }    
    
}
