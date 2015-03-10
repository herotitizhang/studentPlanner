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
        
        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                    ServerCommunicator.generateLoginRequest(usernameInput.getText(), passwordInput.getText()));
            if (serverResponse.isAccepted()) { 
                //to do: load schedule
                ApplicationControl.getInstance().openMainWindow();
                ApplicationControl.getInstance().closeWindow(usernameInput);
            } else if (serverResponse == null) {
                ApplicationControl.getInstance().openSimpleDialog("No response from server.");
            } else {
                ApplicationControl.getInstance().openSimpleDialog("Server rejected input.");
            }
        } catch (IOException ex) {
            ApplicationControl.getInstance().openSimpleDialog("Problem connecting to internet.");
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Creates a new account
     */
    @FXML
    protected void handleCreateButtonAction() {
        
        if (ServerCommunicator.isLoggedIn()) {
            ApplicationControl.getInstance().openSimpleDialog("You are already logged in, can't create new account");
        } else {
            try {
                ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                        ServerCommunicator.generateCreateRequest(usernameInput.getText(), passwordInput.getText()));
                if (serverResponse == null) {
                    ApplicationControl.getInstance().openSimpleDialog("No response from server.");
                } else if (serverResponse.isAccepted()) {
                    // to do: log in, load schedule
                    ApplicationControl.getInstance().openMainWindow();
                    ApplicationControl.getInstance().closeWindow(usernameInput);
                } else {
                    ApplicationControl.getInstance().openSimpleDialog("Server rejected: "+serverResponse.getFailureNotice());
                }
            } catch (IOException e) {
                ApplicationControl.getInstance().openSimpleDialog("Problem connecting to internet.");
            }
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
