/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.DataHandler;
import gui.EventCell;
import gui.ItemNotFoundException;
import java.net.URL;
import java.util.Calendar;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.EventI;
import model.EventI.Priority;
import model.EventI.Repeat;

/**
 * FXML Controller class for displaying an event
 *
 * @author anniefifer1
 */
public class DisplayEventFXMLController implements Initializable {
    
    @FXML TextField nameInput;
    @FXML TextField textInput;
    @FXML DatePicker startDateInput;
    @FXML TextField startHourInput;
    @FXML TextField startMinuteInput;
    @FXML DatePicker endDateInput;
    @FXML TextField endHourInput;
    @FXML TextField endMinuteInput;
    @FXML ComboBox<Repeat> repeatInput;
    @FXML ComboBox<Priority> priorityInput;
    @FXML CheckBox alertBoolInput;
    @FXML DatePicker alertDateInput;
    @FXML TextField alertHourInput;
    @FXML TextField alertMinuteInput;
    @FXML TextField alertTextInput;
    @FXML TextField categoryInput;
    @FXML Button submitButton;
    @FXML ListView eventListView;
    
    /**
     * Triggered when user elects to delete selected event
     */
    @FXML
    private void handleDeleteButtonAction() {
        try {
            closeWindow();
            /* refresh eventListView */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/DashboardViewFXML.fxml"));
            Parent root = (Parent) loader.load();
            DashboardViewFXMLController controller = loader.getController();
            controller.resetEventListView();
            DataHandler.getInstance().deleteCurrentEvent();
        } catch (ItemNotFoundException e) {
            System.err.println("Oops, there's no event selected.");
        } catch (Exception e) {
            System.err.println("Probably an issue loading fxml doc");
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
        try {
            closeWindow();
            /* refresh eventListView */
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/fxml/DashboardViewFXML.fxml"));
            Parent root = (Parent) loader.load();
            DashboardViewFXMLController controller = loader.getController();
            if (DataHandler.getInstance().updateEvent(nameInput.getText(), textInput.getText(), startDateInput.getValue(),
                startHourInput.getText(), startMinuteInput.getText(), endDateInput.getValue(), endHourInput.getText(),
                endMinuteInput.getText(), alertBoolInput.isSelected(), alertTextInput.getText(), alertDateInput.getValue(), 
                alertHourInput.getText(), alertMinuteInput.getText(), repeatInput.getValue(), priorityInput.getValue(),
                DataHandler.getInstance().getCategory(categoryInput.getText()))) {
                    EventCell cell = (EventCell) controller.eventListView.getSelectionModel().getSelectedItem();
                    controller.resetEventListView();
            } else {
                //event couldn't be updated
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Sets the content of the display to the selected event's information
     * @param event 
     */
    private void setContent(EventI event) {
        nameInput.setText(event.getName());
        textInput.setText(event.getText());
        startDateInput.setValue(DataHandler.getInstance().calendarToDate(event.getStartTime()));
        startHourInput.setText(addLeadingZero(Integer.toString(event.getStartTime().get(Calendar.HOUR))));
        startMinuteInput.setText(addLeadingZero(Integer.toString(event.getStartTime().get(Calendar.MINUTE))));
        endDateInput.setValue(DataHandler.getInstance().calendarToDate(event.getEndTime()));
        endHourInput.setText(addLeadingZero(Integer.toString(event.getEndTime().get(Calendar.HOUR))));
        endMinuteInput.setText(addLeadingZero(Integer.toString(event.getEndTime().get(Calendar.MINUTE))));
        alertBoolInput.setSelected(event.hasAlert());
        if (event.hasAlert()) {
            alertDateInput.setValue(DataHandler.getInstance().calendarToDate(event.getAlertTime()));
            alertHourInput.setText(addLeadingZero(Integer.toString(event.getAlertTime().get(Calendar.HOUR))));
            alertMinuteInput.setText(addLeadingZero(Integer.toString(event.getAlertTime().get(Calendar.MINUTE))));
            alertTextInput.setText(event.getAlertText());
        }
        categoryInput.setText(event.getCategory().toString());
        priorityInput.setValue(event.getPriority());
        repeatInput.setValue(event.getRepeating());
    }
    
    /**
     * Controls ability to edit displayed event information
     * @param editable
     */
    private void setEditableFields(boolean editable) {
        nameInput.setEditable(editable);
        textInput.setEditable(editable);
        startDateInput.setEditable(editable);
        startHourInput.setEditable(editable);
        startMinuteInput.setEditable(editable);
        endDateInput.setEditable(editable);
        endHourInput.setEditable(editable);
        endMinuteInput.setEditable(editable);
        alertBoolInput.setDisable(!editable);
        alertDateInput.setEditable(editable);
        alertHourInput.setEditable(editable);
        alertMinuteInput.setEditable(editable);
        alertTextInput.setEditable(editable);
        categoryInput.setEditable(editable);
        priorityInput.setEditable(editable);
        repeatInput.setEditable(editable);
    }
    
    private String addLeadingZero(String str) {
        if (str.length() == 1) {
            str = "0" + str;
        }
        return str;
    }
    
    private void closeWindow() {
        Scene scene = nameInput.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
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
            closeWindow();
        }
    }    
    
}
