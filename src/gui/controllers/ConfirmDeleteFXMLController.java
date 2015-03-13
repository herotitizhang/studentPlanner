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
import javafx.scene.control.Button;
import model.EventI;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class ConfirmDeleteFXMLController implements Initializable {
    
    EventI event;
    @FXML Button cancelButton;
    
    @FXML
    protected void handleConfirmButtonAction() {
        DataHandler.getInstance().removeEvent(event);
        ApplicationControl.getInstance().closeWindow(cancelButton);
    }
    
    @FXML
    protected void handleCancelButtonAction() {
        ApplicationControl.getInstance().closeWindow(cancelButton);
    }
    
    public void setEvent(EventI e) {
        event = e;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
