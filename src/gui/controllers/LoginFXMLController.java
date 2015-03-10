/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import backendIO.ServerCommunicator;
import backendIO.ServerResponse;
import gui.ApplicationControl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class LoginFXMLController implements Initializable {
    
    @FXML TextField usernameInput;
    @FXML PasswordField passwordInput;
    
    /**
     * Requests server login with username/password
     */
    @FXML
    protected void handleLogInButtonAction() {
        
        if (ApplicationControl.getInstance().LogInUser(usernameInput.getText(), passwordInput.getText())) {
            ApplicationControl.getInstance().closeWindow(usernameInput);
        }
    }
    
    /**
     * Creates a new account
     */
    @FXML
    protected void handleCreateButtonAction() {
        
        if (ApplicationControl.getInstance().CreateAndLogInUser(usernameInput.getText(), passwordInput.getText())) {
            ApplicationControl.getInstance().closeWindow(usernameInput);
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
