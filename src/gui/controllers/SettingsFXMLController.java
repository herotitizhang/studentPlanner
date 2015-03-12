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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class SettingsFXMLController implements Initializable {

    @FXML Label verificationLabel;
    @FXML TextField verificationInput;
    @FXML Button submitButton;
    @FXML Button requestAuthButton;
    
    @FXML
    protected void handleSubmitButtonAction() {
        if (!verificationInput.getText().isEmpty()) {
            ApplicationControl.getInstance().authenticatePhone(verificationInput.getText());
        }
    }
    
    @FXML
    protected void handleRequestAuthButtonAction() {
        ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/RequestAuthFXML.fxml");
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*
        if (ServerCommunicator.getPhoneNumber() == null) {
            authLabel.setVisible(false);
            authInput.setVisible(false);
        } else if (ServerCommunicator.isAuthenticated()) {
            authLabel.setVisible(false);
            authInput.setVisible(false);
            submitButton.setVisible(false);
        }
                */
    }    
    
}
