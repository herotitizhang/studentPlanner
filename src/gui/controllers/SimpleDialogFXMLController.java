/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import gui.ApplicationControl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class SimpleDialogFXMLController implements Initializable {

    @FXML Label messageLabel;
    
    @FXML
    protected void closeWindow() {
        ApplicationControl.getInstance().closeWindow(messageLabel);
    }
    
    public void setMessage(String msg) {
        messageLabel.setText(msg);
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
