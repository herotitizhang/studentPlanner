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
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class RequestAuthFXMLController implements Initializable {

    @FXML TextField phoneInput;
    
    @FXML
    protected void handleRequestButtonAction() {
        if (ApplicationControl.getInstance().requestPhoneAuthentication(phoneInput.getText())) {
            ApplicationControl.getInstance().openSimpleDialog("Number has been changed, expect a text soon!");
        } else {
            ApplicationControl.getInstance().openSimpleDialog("There was an error, please try again later.");
        }
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
