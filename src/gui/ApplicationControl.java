/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import backendIO.ClientIOSystem;
import backendIO.ServerCommunicator;
import backendIO.ServerResponse;
import gui.controllers.AddEventFXMLController;
import gui.controllers.LoginFXMLController;
import gui.controllers.SimpleDialogFXMLController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ScheduleI;

/**
 *
 * @author anniefifer1
 */
public class ApplicationControl {
    
    private final static ApplicationControl instance = new ApplicationControl();
    
    public boolean onlineMode = true;
    
    public ApplicationControl() {
        
    }
    
    public static ApplicationControl getInstance() {
        return instance;
    }
    
    public void setOnlineMode(boolean mode) {
        onlineMode = mode;
    }
    
    public boolean isOnline() {
        return onlineMode;
    }
    
    public void initiateLogin() throws IOException {
        if (ServerCommunicator.checkConnection()) {
            openFXMLWindow("/gui/fxml/LoginFXML.fxml");
        } else {
            openSimpleDialog("No connection");
        }
    }
    
    public void openMainWindow() {
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
    
    public Object openFXMLWindow(String filePath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(filePath));
            Parent root = (Parent) loader.load();
            Stage stage = new Stage();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
            Object controller = loader.getController();
            return controller;
        } catch (IOException ex) {
            Logger.getLogger(ApplicationControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public Object getController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = (Parent) loader.load();
            return loader.getController();
        } catch (IOException ex) {
            Logger.getLogger(ApplicationControl.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public void refreshData() {
        
    }
    
    public void openSimpleDialog(String msg) {
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
    
    public void closeWindow(Node node) {
        Scene scene = node.getScene();
        Stage stage = (Stage) scene.getWindow();
        stage.close();
    }

    public void save() {
        
        Boolean connection = false;
        
        if (!ServerCommunicator.checkConnection()) {
            openSimpleDialog("Sorry no connection is available, cannot save.");
        }

        if (!ServerCommunicator.isLoggedIn()) {
            // Not sure this will happen
        } 

        try {
            ServerResponse serverResponse = ServerCommunicator.sendClientRequest(
                    ServerCommunicator.generateSaveRequest(DataHandler.getInstance().getSchedule()));
            if (serverResponse == null) {
                openSimpleDialog("No response from the server! You schedule is not saved.");
            }

            // if saved sucessfully
            if (serverResponse.isAccepted()) {
                // also save locally here
            } else {
                openSimpleDialog("Sorry, server was not able to save your schedule");
            }
            System.out.println();
        } catch (IOException e) {
            openSimpleDialog("Sorry, can't connect to the internet!");
        }
        
    }

    public void loadSchedule(String fileName) {
        ScheduleI tempSchedule = ClientIOSystem.loadFromDisk(fileName);
        if (tempSchedule != null) {
            DataHandler.getInstance().setSchedule(tempSchedule);
            // refresh content
	}
    }
    
}
