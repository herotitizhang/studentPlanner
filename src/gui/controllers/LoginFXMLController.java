/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.controllers;

import backendIO.ServerCommunicator;
import backendIO.ServerResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
        /*
        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                    ServerCommunicator.generateLoginRequest(usernameInput.getText(), passwordInput.getText()));
            if (serverResponse.isAccepted()) { 
                //to do: load schedule
                loadMainWindow();
                closeWindow();
            } else if (serverResponse == null) {
                openSimpleDialog("No response from server.");
            } else {
                openSimpleDialog("Server rejected input.");
            }
        } catch (IOException ex) {
            openSimpleDialog("Problem connecting to internet.");
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        */
        loadMainWindow();
        closeWindow();
    }
    
    /**
     * Creates a new account
     */
    @FXML
    protected void handleCreateButtonAction() {
        /*
        if (ServerCommunicator.isLoggedIn()) {
            openSimpleDialog("You are already logged in, can't create new account");
        } else {
            try {
                ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                        ServerCommunicator.generateCreateRequest(usernameInput.getText(), passwordInput.getText()));
                if (serverResponse == null) {
                    openSimpleDialog("No response from server.");
                } else if (serverResponse.isAccepted()) {
                    // to do: log in, load schedule
                    loadMainWindow();
                    closeWindow();
                } else {
                    openSimpleDialog("Server rejected: "+serverResponse.getFailureNotice());
                }
            } catch (IOException e) {
                openSimpleDialog("Problem connecting to internet.");
            }
        }
        */
        loadMainWindow();
        closeWindow();
    }
    
    private void closeWindow() {
        Scene scene = usernameInput.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
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
    
    private void loadMainWindow() {
        try {
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/gui/fxml/MainFXML.fxml"));
            /* These will be moved to local stylesheets */
            Scene scene = new Scene(root);
            scene.getStylesheets().addAll("http://fonts.googleapis.com/css?family=Slabo+27px",
                    "http://fonts.googleapis.com/css?family=Oswald",
                    "http://fonts.googleapis.com/css?family=Pontano+Sans");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(LoginFXMLController.class.getName()).log(Level.SEVERE, null, ex);
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
