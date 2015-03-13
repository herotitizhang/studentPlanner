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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author anniefifer1
 */
public class LoginFXMLController implements Initializable {
    
    @FXML TextField ipInput;
    @FXML TextField usernameInput;
    @FXML PasswordField passwordInput;
    @FXML Label phoneLabel;
    @FXML TextField phoneInput;
    @FXML Button createButton;
    @FXML Button loginButton;
    
    /**
     * Requests server login with username/password
     */
    @FXML
    protected void handleLogInButtonAction() {	
        if (!ApplicationControl.getInstance().setIP(ipInput.getText())) { /* check for no connection with ip here */
            ApplicationControl.getInstance().openSimpleDialog("Please enter a valid IP.");
            return;
        }
        if (!ServerCommunicator.checkConnection()) {
            ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/OfflineWarningFXML.fxml");
        } else if (ApplicationControl.getInstance().LogInUser(usernameInput.getText(), passwordInput.getText())) {
            ApplicationControl.getInstance().closeWindow(usernameInput);
            ApplicationControl.getInstance().loadApplication();
        } 
       	
    }
    
    @FXML
    protected void handleNewButtonAction() {
        phoneInput.setVisible(true);
        phoneLabel.setVisible(true);
        loginButton.setVisible(false);
        createButton.setText("Create");
        createButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                handleCreateButtonAction();
            }
        });
    }
    
    public void closeWindow() {
        ApplicationControl.getInstance().closeWindow(ipInput);
    }
    
    /**
     * Creates a new account
     */
    @FXML
    protected void handleCreateButtonAction() {
        if (!ApplicationControl.getInstance().setIP(ipInput.getText())) { /* check for no connection with ip here */
            ApplicationControl.getInstance().openSimpleDialog("Please enter a valid IP.");
            return;
        }
        if (ApplicationControl.getInstance().CreateAndLogInUser(usernameInput.getText(), passwordInput.getText(),
            phoneInput.getText())) {
            ApplicationControl.getInstance().closeWindow(usernameInput);
            ApplicationControl.getInstance().loadApplication();
        } else {
            ApplicationControl.getInstance().openFXMLWindow("/gui/fxml/OfflineWarningFXML.fxml");
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
