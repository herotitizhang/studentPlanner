/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import gui.ItemNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Event;
import model.EventI;
import model.EventI.Repeat;

/**
 * FXML Controller class for displaying an event
 *
 * @author anniefifer1
 */
public class DisplayEventFXMLController implements Initializable {
    
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
     * Triggered when user elects to delete selected event
     */
    @FXML
    private void handleDeleteButtonAction() {
        try {
            DataHandler.getInstance().deleteCurrentEvent();
        } catch (ItemNotFoundException e) {
            System.err.println("Oops, there's no event selected.");
        }
    }
    
    /**
     * Triggered when user elects to edit selected event
     */
    @FXML
    private void handleEditButtonAction() {
        setEditableFields(true);
        submitButton.setVisible(true);
    }
    
    /**
     * Triggered when user submits updated information after editing
     */
    @FXML
    private void handleSubmitButtonAction() {
        DataHandler.getInstance().updateEvent(nameInput.getText(), textInput.getText(), startTimeInput.getText(), 
                endTimeInput.getText(), alertBoolInput.isSelected(), alertTextInput.getText(), 
                alertTimeInput.getText(), Repeat.NONE, DataHandler.getInstance().getCategory(categoryInput.getText()));
        setEditableFields(false);
    }
    
    /**
     * Sets the content of the display to the selected event's information
     * @param event 
     */
    private void setContent(EventI event) {
        nameInput.setText(event.getName());
        textInput.setText(event.getText());
        startTimeInput.setText(event.getStartTime().toString());
        endTimeInput.setText(event.getEndTime().toString());
        alertBoolInput.setSelected(event.hasAlert());
        alertTimeInput.setText(event.getAlertTime().toString());
        alertTextInput.setText(event.getAlertText());
        categoryInput.setText(event.getCategory().toString());
    }
    
    /**
     * Controls ability to edit displayed event information
     * @param editable
     */
    private void setEditableFields(boolean editable) {
        nameInput.setEditable(editable);
        textInput.setEditable(editable);
        startTimeInput.setEditable(editable);
        endTimeInput.setEditable(editable);
        alertBoolInput.setDisable(!editable);
        alertTimeInput.setEditable(editable);
        alertTextInput.setEditable(editable);
        categoryInput.setEditable(editable);
    }

    /**
     * Initializes the controller class by setting content and disabling editing
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            EventI event = DataHandler.getInstance().getCurrentEvent();
            setContent(event);
            setEditableFields(false);
            submitButton.setVisible(false);
        } catch (ItemNotFoundException e) {
            System.err.println("Sorry, an error occured");
            Scene scene = nameInput.getScene();
            Stage stage = (Stage) scene.getWindow();
            stage.close();
        }
    }    
    
}
